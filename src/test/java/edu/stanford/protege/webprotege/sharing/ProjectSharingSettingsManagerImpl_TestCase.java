package edu.stanford.protege.webprotege.sharing;

import edu.stanford.protege.webprotege.MockingUtils;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.authorization.ProjectResource;
import edu.stanford.protege.webprotege.authorization.RoleId;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.user.UserDetailsManager;
import edu.stanford.protege.webprotege.user.UserLookupException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static edu.stanford.protege.webprotege.authorization.Subject.forUser;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Covers {@link ProjectSharingSettingsManagerImpl#setProjectSharingSettings(UserId, ProjectSharingSettings)}.
 * Locks in both the acting-user safety net (GH #176) and the per-entry lookup-failure handling
 * (GH #179) together, since the two behaviours interact.
 * <p>
 * The sharing settings map is built from a plain {@link java.util.HashMap} internally, so iteration
 * order is not deterministic - assertions below only check order-independent invariants (which
 * entries got roles, whether the safety net ran, whether the call threw).
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ProjectSharingSettingsManagerImpl_TestCase {

    @Mock
    private AccessManager accessManager;

    @Mock
    private UserDetailsManager userLookup;

    private ProjectSharingSettingsManagerImpl manager;

    private final ProjectId projectId = MockingUtils.mockProjectId();

    private final ProjectResource projectResource = new ProjectResource(projectId);

    private final UserId actingUserId = MockingUtils.mockUserId();

    @BeforeEach
    public void setUp() {
        manager = new ProjectSharingSettingsManagerImpl(accessManager, userLookup);
    }

    @Test
    public void shouldSkipConfirmedNotFoundEntryWithoutThrowing() {
        PersonId unresolvedPersonId = PersonId.get("unresolved-person");
        when(userLookup.getUserByUserIdOrEmail(unresolvedPersonId.getId())).thenReturn(Optional.empty());

        ProjectSharingSettings settings = new ProjectSharingSettings(
                projectId,
                Optional.empty(),
                List.of(new SharingSetting(unresolvedPersonId, SharingPermission.EDIT)));

        manager.setProjectSharingSettings(actingUserId, settings);

        verify(userLookup).getUserByUserIdOrEmail(unresolvedPersonId.getId());
        // No lookup resolved to a specific user, and the acting user had no prior access
        // (unstubbed getAssignedRoles returns empty), so the safety net must not have fired either.
        verify(accessManager, never()).setAssignedRoles(argThat(subject -> subject.getUserId().isPresent()),
                                                          eq(projectResource),
                                                          any());
    }

    @Test
    public void shouldContinueProcessingRemainingEntriesAndThrowWhenOneEntryLookupFails() {
        UserId resolvedUserId = MockingUtils.mockUserId();
        PersonId failingPersonId = PersonId.get("failing-person");
        PersonId resolvedPersonId = PersonId.of(resolvedUserId);
        UserLookupException lookupException = new UserLookupException("boom", new RuntimeException("cause"));

        when(userLookup.getUserByUserIdOrEmail(failingPersonId.getId())).thenThrow(lookupException);
        when(userLookup.getUserByUserIdOrEmail(resolvedPersonId.getId())).thenReturn(Optional.of(resolvedUserId));

        Set<RoleId> actingUserRolesBeforeChange = Set.of(new RoleId("PreExistingRole"));
        when(accessManager.getAssignedRoles(forUser(actingUserId), projectResource))
                .thenReturn(actingUserRolesBeforeChange);

        ProjectSharingSettings settings = new ProjectSharingSettings(
                projectId,
                Optional.empty(),
                List.of(new SharingSetting(failingPersonId, SharingPermission.EDIT),
                        new SharingSetting(resolvedPersonId, SharingPermission.VIEW)));

        RuntimeException thrown = assertThrows(RuntimeException.class,
                                                () -> manager.setProjectSharingSettings(actingUserId, settings));
        assertThat(thrown, is(lookupException));

        // The other entry was still processed even though the first one's lookup failed.
        verify(accessManager).setAssignedRoles(eq(forUser(resolvedUserId)),
                                                eq(projectResource),
                                                eq(Roles.fromSharingPermission(SharingPermission.VIEW)));
        // The acting-user safety net still ran unconditionally (they were not part of the settings).
        verify(accessManager).setAssignedRoles(eq(forUser(actingUserId)),
                                                eq(projectResource),
                                                eq(new HashSet<>(actingUserRolesBeforeChange)));
    }

    @Test
    public void shouldRestoreActingUsersAccessAndThrowWhenActingUsersOwnLookupFails() {
        PersonId actingUserPersonId = PersonId.of(actingUserId);
        UserLookupException lookupException = new UserLookupException("boom", new RuntimeException("cause"));
        when(userLookup.getUserByUserIdOrEmail(actingUserPersonId.getId())).thenThrow(lookupException);

        Set<RoleId> actingUserRolesBeforeChange = Set.of(new RoleId("PreExistingRole"));
        when(accessManager.getAssignedRoles(forUser(actingUserId), projectResource))
                .thenReturn(actingUserRolesBeforeChange);

        ProjectSharingSettings settings = new ProjectSharingSettings(
                projectId,
                Optional.empty(),
                List.of(new SharingSetting(actingUserPersonId, SharingPermission.EDIT)));

        RuntimeException thrown = assertThrows(RuntimeException.class,
                                                () -> manager.setProjectSharingSettings(actingUserId, settings));
        assertThat(thrown, is(lookupException));

        // Safety net restored the acting user's pre-existing access even though their own entry
        // failed to resolve.
        verify(accessManager).setAssignedRoles(eq(forUser(actingUserId)),
                                                eq(projectResource),
                                                eq(new HashSet<>(actingUserRolesBeforeChange)));
    }

    @Test
    public void shouldNotOverwriteActingUsersOwnRolesWhenTheirOwnEntryResolvesSuccessfully() {
        PersonId actingUserPersonId = PersonId.of(actingUserId);
        when(userLookup.getUserByUserIdOrEmail(actingUserPersonId.getId())).thenReturn(Optional.of(actingUserId));

        Set<RoleId> actingUserRolesBeforeChange = Set.of(new RoleId("PreExistingRole"));
        when(accessManager.getAssignedRoles(forUser(actingUserId), projectResource))
                .thenReturn(actingUserRolesBeforeChange);

        ProjectSharingSettings settings = new ProjectSharingSettings(
                projectId,
                Optional.empty(),
                List.of(new SharingSetting(actingUserPersonId, SharingPermission.VIEW)));

        manager.setProjectSharingSettings(actingUserId, settings);

        // The acting user's entry resolved and their submitted role was applied - the safety net
        // must not have separately restored their pre-existing role on top of / instead of it.
        verify(accessManager).setAssignedRoles(eq(forUser(actingUserId)),
                                                eq(projectResource),
                                                eq(Roles.fromSharingPermission(SharingPermission.VIEW)));
        verify(accessManager, never()).setAssignedRoles(eq(forUser(actingUserId)),
                                                          eq(projectResource),
                                                          eq(new HashSet<>(actingUserRolesBeforeChange)));
    }

    @Test
    public void shouldContinueProcessingRemainingEntriesAndThrowWhenOneEntryIsADuplicateMatch() {
        UserId resolvedUserId = MockingUtils.mockUserId();
        PersonId duplicatePersonId = PersonId.get("duplicate-person");
        PersonId resolvedPersonId = PersonId.of(resolvedUserId);
        RuntimeException duplicateUserException = new RuntimeException("Duplicated user with username duplicate-person");

        when(userLookup.getUserByUserIdOrEmail(duplicatePersonId.getId())).thenThrow(duplicateUserException);
        when(userLookup.getUserByUserIdOrEmail(resolvedPersonId.getId())).thenReturn(Optional.of(resolvedUserId));

        Set<RoleId> actingUserRolesBeforeChange = Set.of(new RoleId("PreExistingRole"));
        when(accessManager.getAssignedRoles(forUser(actingUserId), projectResource))
                .thenReturn(actingUserRolesBeforeChange);

        ProjectSharingSettings settings = new ProjectSharingSettings(
                projectId,
                Optional.empty(),
                List.of(new SharingSetting(duplicatePersonId, SharingPermission.EDIT),
                        new SharingSetting(resolvedPersonId, SharingPermission.VIEW)));

        // A duplicate-match RuntimeException (a data-integrity problem, not a UserLookupException)
        // is deliberately given the same treatment as an infra failure at this call site: the entry
        // is skipped, remaining entries are still processed, the safety net still runs, and the
        // failure still surfaces at the end - see GH #179 spec Design Notes.
        RuntimeException thrown = assertThrows(RuntimeException.class,
                                                () -> manager.setProjectSharingSettings(actingUserId, settings));
        assertThat(thrown, is(duplicateUserException));

        verify(accessManager).setAssignedRoles(eq(forUser(resolvedUserId)),
                                                eq(projectResource),
                                                eq(Roles.fromSharingPermission(SharingPermission.VIEW)));
        verify(accessManager).setAssignedRoles(eq(forUser(actingUserId)),
                                                eq(projectResource),
                                                eq(new HashSet<>(actingUserRolesBeforeChange)));
    }

    @Test
    public void shouldRestoreActingUsersAccessWithoutThrowingWhenActingUserNotInSettingsAndNoLookupFailures() {
        UserId otherUserId = MockingUtils.mockUserId();
        PersonId otherPersonId = PersonId.of(otherUserId);
        when(userLookup.getUserByUserIdOrEmail(otherPersonId.getId())).thenReturn(Optional.of(otherUserId));

        Set<RoleId> actingUserRolesBeforeChange = Set.of(new RoleId("PreExistingRole"));
        when(accessManager.getAssignedRoles(forUser(actingUserId), projectResource))
                .thenReturn(actingUserRolesBeforeChange);

        ProjectSharingSettings settings = new ProjectSharingSettings(
                projectId,
                Optional.empty(),
                List.of(new SharingSetting(otherPersonId, SharingPermission.VIEW)));

        manager.setProjectSharingSettings(actingUserId, settings);

        verify(accessManager).setAssignedRoles(eq(forUser(otherUserId)),
                                                eq(projectResource),
                                                eq(Roles.fromSharingPermission(SharingPermission.VIEW)));
        verify(accessManager).setAssignedRoles(eq(forUser(actingUserId)),
                                                eq(projectResource),
                                                eq(new HashSet<>(actingUserRolesBeforeChange)));
    }
}
