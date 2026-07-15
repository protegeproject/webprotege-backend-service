package edu.stanford.protege.webprotege.sharing;

import edu.stanford.protege.webprotege.MockingUtils;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.authorization.ProjectResource;
import edu.stanford.protege.webprotege.authorization.RoleId;
import edu.stanford.protege.webprotege.authorization.Subject;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static edu.stanford.protege.webprotege.access.BuiltInRole.CAN_EDIT;
import static edu.stanford.protege.webprotege.access.BuiltInRole.CAN_VIEW;
import static edu.stanford.protege.webprotege.authorization.Subject.forAnySignedInUser;
import static edu.stanford.protege.webprotege.authorization.Subject.forUser;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Covers {@link ProjectSharingSettingsManagerImpl#setProjectSharingSettings(UserId, ProjectSharingSettings)}.
 * <p>
 * The implementation diffs the submitted settings against the project's current sharing state
 * (keyed on resolved {@link UserId}, never on raw {@link PersonId}) rather than wiping and
 * re-granting every subject's access. These tests lock in that diff behaviour together with the
 * per-entry lookup-failure handling from GH #179 and the removal-gating / acting-user-exclusion /
 * non-sharing-role-preservation rules from GH #178, since they all interact.
 * <p>
 * The sharing settings map is built from a plain {@link java.util.HashMap} internally, so iteration
 * order is not deterministic - assertions below only check order-independent invariants (which
 * entries got roles, which did not, whether the call threw).
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

    /**
     * Subjects accumulated so far by {@link #givenCurrentAccess(UserId, RoleId...)}, backing the
     * stub for {@link AccessManager#getSubjectsWithAccessToResource(edu.stanford.protege.webprotege.authorization.Resource)}.
     */
    private final List<Subject> currentSubjects = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        manager = new ProjectSharingSettingsManagerImpl(accessManager, userLookup);
    }

    /**
     * Registers {@code userId} as currently holding {@code roleIds} on {@link #projectResource}.
     * <p>
     * The implementation under test populates its notion of "current access" purely by iterating
     * whatever {@link AccessManager#getSubjectsWithAccessToResource} returns and then calling
     * {@link AccessManager#getAssignedRoles} for each such subject - stubbing only
     * {@code getAssignedRoles} for a user is not enough, since that user would never be discovered
     * in the first place. This helper stubs both consistently, accumulating subjects across calls
     * so a test can build up current state incrementally.
     */
    private void givenCurrentAccess(UserId userId, RoleId... roleIds) {
        Subject subject = forUser(userId);
        currentSubjects.add(subject);
        when(accessManager.getSubjectsWithAccessToResource(projectResource)).thenReturn(currentSubjects);
        when(accessManager.getAssignedRoles(subject, projectResource)).thenReturn(Set.of(roleIds));
    }

    // ----------------------------------------------------------------------------------------
    // Lookup resolution (GH #179) - rewritten to assert against the new diff-based behaviour.
    // ----------------------------------------------------------------------------------------

    @Test
    public void shouldSkipConfirmedNotFoundEntryWithoutThrowing() {
        PersonId unresolvedPersonId = PersonId.get("unresolved-person");
        when(userLookup.getUserByUserIdOrEmail(unresolvedPersonId.getId())).thenReturn(Optional.empty());

        ProjectSharingSettings settings = new ProjectSharingSettings(
                projectId,
                Optional.empty(),
                List.of(new SharingSetting(unresolvedPersonId, SharingPermission.EDIT)));

        assertDoesNotThrow(() -> manager.setProjectSharingSettings(actingUserId, settings));

        verify(userLookup).getUserByUserIdOrEmail(unresolvedPersonId.getId());
        // Nothing resolved to a specific user, there is no current access to diff against, and
        // link sharing was not submitted and was not previously set - nothing to do at all.
        verify(accessManager, never()).setAssignedRoles(any(), eq(projectResource), any());
    }

    @Test
    public void shouldContinueProcessingRemainingEntriesAndThrowWhenOneEntryLookupFails() {
        UserId resolvedUserId = MockingUtils.mockUserId();
        PersonId failingPersonId = PersonId.get("failing-person");
        PersonId resolvedPersonId = PersonId.of(resolvedUserId);
        UserLookupException lookupException = new UserLookupException("boom", new RuntimeException("cause"));

        when(userLookup.getUserByUserIdOrEmail(failingPersonId.getId())).thenThrow(lookupException);
        when(userLookup.getUserByUserIdOrEmail(resolvedPersonId.getId())).thenReturn(Optional.of(resolvedUserId));

        givenCurrentAccess(actingUserId, new RoleId("PreExistingRole"));

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
        // The acting user is never a removal candidate, and a lookup failure occurred, so removals
        // were not even computed - either way, the acting user must not have been touched at all.
        verify(accessManager, never()).setAssignedRoles(eq(forUser(actingUserId)), eq(projectResource), any());
    }

    @Test
    public void shouldLeaveActingUsersAccessUntouchedWhenActingUsersOwnLookupFails() {
        PersonId actingUserPersonId = PersonId.of(actingUserId);
        UserLookupException lookupException = new UserLookupException("boom", new RuntimeException("cause"));
        when(userLookup.getUserByUserIdOrEmail(actingUserPersonId.getId())).thenThrow(lookupException);

        givenCurrentAccess(actingUserId, new RoleId("PreExistingRole"));

        ProjectSharingSettings settings = new ProjectSharingSettings(
                projectId,
                Optional.empty(),
                List.of(new SharingSetting(actingUserPersonId, SharingPermission.EDIT)));

        RuntimeException thrown = assertThrows(RuntimeException.class,
                                                () -> manager.setProjectSharingSettings(actingUserId, settings));
        assertThat(thrown, is(lookupException));

        // The acting user is never a removal candidate - since their own entry failed to resolve,
        // it is as if they were not submitted at all, and their access is left completely alone.
        verify(accessManager, never()).setAssignedRoles(eq(forUser(actingUserId)), eq(projectResource), any());
    }

    @Test
    public void shouldNotOverwriteActingUsersOwnRolesWhenTheirOwnEntryResolvesSuccessfully() {
        PersonId actingUserPersonId = PersonId.of(actingUserId);
        when(userLookup.getUserByUserIdOrEmail(actingUserPersonId.getId())).thenReturn(Optional.of(actingUserId));

        givenCurrentAccess(actingUserId, new RoleId("PreExistingRole"));

        ProjectSharingSettings settings = new ProjectSharingSettings(
                projectId,
                Optional.empty(),
                List.of(new SharingSetting(actingUserPersonId, SharingPermission.VIEW)));

        manager.setProjectSharingSettings(actingUserId, settings);

        // The acting user's own entry resolved with a new permission - it is applied like any other
        // update, preserving their pre-existing non-sharing role alongside the new sharing role.
        Set<RoleId> expectedRoles = new HashSet<>(Set.of(new RoleId("PreExistingRole")));
        expectedRoles.addAll(Roles.fromSharingPermission(SharingPermission.VIEW));
        verify(accessManager).setAssignedRoles(eq(forUser(actingUserId)), eq(projectResource), eq(expectedRoles));
        // ... and that was the only call made for the acting user - no separate restore of their
        // old role set on top of / instead of it.
        verify(accessManager, times(1))
                .setAssignedRoles(eq(forUser(actingUserId)), eq(projectResource), any());
    }

    @Test
    public void shouldContinueProcessingRemainingEntriesAndThrowWhenOneEntryIsADuplicateMatch() {
        UserId resolvedUserId = MockingUtils.mockUserId();
        PersonId duplicatePersonId = PersonId.get("duplicate-person");
        PersonId resolvedPersonId = PersonId.of(resolvedUserId);
        RuntimeException duplicateUserException = new RuntimeException("Duplicated user with username duplicate-person");

        when(userLookup.getUserByUserIdOrEmail(duplicatePersonId.getId())).thenThrow(duplicateUserException);
        when(userLookup.getUserByUserIdOrEmail(resolvedPersonId.getId())).thenReturn(Optional.of(resolvedUserId));

        givenCurrentAccess(actingUserId, new RoleId("PreExistingRole"));

        ProjectSharingSettings settings = new ProjectSharingSettings(
                projectId,
                Optional.empty(),
                List.of(new SharingSetting(duplicatePersonId, SharingPermission.EDIT),
                        new SharingSetting(resolvedPersonId, SharingPermission.VIEW)));

        // A duplicate-match RuntimeException (a data-integrity problem, not a UserLookupException)
        // is deliberately given the same treatment as an infra failure at this call site: the entry
        // is skipped, remaining entries are still processed, and the failure still surfaces at the
        // end - see GH #179 spec Design Notes.
        RuntimeException thrown = assertThrows(RuntimeException.class,
                                                () -> manager.setProjectSharingSettings(actingUserId, settings));
        assertThat(thrown, is(duplicateUserException));

        verify(accessManager).setAssignedRoles(eq(forUser(resolvedUserId)),
                                                eq(projectResource),
                                                eq(Roles.fromSharingPermission(SharingPermission.VIEW)));
        verify(accessManager, never()).setAssignedRoles(eq(forUser(actingUserId)), eq(projectResource), any());
    }

    @Test
    public void shouldLeaveActingUsersAccessUntouchedWithoutThrowingWhenActingUserNotInSettingsAndNoLookupFailures() {
        UserId otherUserId = MockingUtils.mockUserId();
        PersonId otherPersonId = PersonId.of(otherUserId);
        when(userLookup.getUserByUserIdOrEmail(otherPersonId.getId())).thenReturn(Optional.of(otherUserId));

        // The acting user currently holds an actual sharing role (not just some unrelated role) -
        // if the acting user were not unconditionally excluded from removal candidates, this would
        // otherwise look exactly like a legitimate removal.
        givenCurrentAccess(actingUserId, CAN_VIEW.getRoleId());

        ProjectSharingSettings settings = new ProjectSharingSettings(
                projectId,
                Optional.empty(),
                List.of(new SharingSetting(otherPersonId, SharingPermission.VIEW)));

        assertDoesNotThrow(() -> manager.setProjectSharingSettings(actingUserId, settings));

        verify(accessManager).setAssignedRoles(eq(forUser(otherUserId)),
                                                eq(projectResource),
                                                eq(Roles.fromSharingPermission(SharingPermission.VIEW)));
        verify(accessManager, never()).setAssignedRoles(eq(forUser(actingUserId)), eq(projectResource), any());
    }

    // ----------------------------------------------------------------------------------------
    // Diff / removal-gating / role-preservation scenarios (GH #178).
    // ----------------------------------------------------------------------------------------

    @Test
    public void shouldSkipSetAssignedRolesForNoOpEntry() {
        UserId collaboratorId = MockingUtils.mockUserId();
        PersonId collaboratorPersonId = PersonId.of(collaboratorId);
        when(userLookup.getUserByUserIdOrEmail(collaboratorPersonId.getId())).thenReturn(Optional.of(collaboratorId));

        givenCurrentAccess(collaboratorId, CAN_VIEW.getRoleId());

        ProjectSharingSettings settings = new ProjectSharingSettings(
                projectId,
                Optional.empty(),
                List.of(new SharingSetting(collaboratorPersonId, SharingPermission.VIEW)));

        assertDoesNotThrow(() -> manager.setProjectSharingSettings(actingUserId, settings));

        verify(accessManager, never()).setAssignedRoles(eq(forUser(collaboratorId)), eq(projectResource), any());
    }

    @Test
    public void shouldClearCollaboratorsSharingRoleWhenAbsentFromSubmittedSettingsAndNoLookupFailures() {
        UserId collaboratorId = MockingUtils.mockUserId();
        givenCurrentAccess(collaboratorId, CAN_VIEW.getRoleId());

        ProjectSharingSettings settings = new ProjectSharingSettings(
                projectId,
                Optional.empty(),
                List.of());

        assertDoesNotThrow(() -> manager.setProjectSharingSettings(actingUserId, settings));

        verify(accessManager).setAssignedRoles(eq(forUser(collaboratorId)), eq(projectResource), eq(Set.of()));
    }

    @Test
    public void shouldNotRemoveCollaboratorWhenAnotherEntryLookupFailsThisCall() {
        UserId collaboratorId = MockingUtils.mockUserId();
        givenCurrentAccess(collaboratorId, CAN_VIEW.getRoleId());

        PersonId failingPersonId = PersonId.get("failing-person");
        UserLookupException lookupException = new UserLookupException("boom", new RuntimeException("cause"));
        when(userLookup.getUserByUserIdOrEmail(failingPersonId.getId())).thenThrow(lookupException);

        ProjectSharingSettings settings = new ProjectSharingSettings(
                projectId,
                Optional.empty(),
                List.of(new SharingSetting(failingPersonId, SharingPermission.EDIT)));

        RuntimeException thrown = assertThrows(RuntimeException.class,
                                                () -> manager.setProjectSharingSettings(actingUserId, settings));
        assertThat(thrown, is(lookupException));

        // The collaborator would otherwise have been dropped (absent from the submitted list), but
        // a failure elsewhere in the same call must block that removal entirely.
        verify(accessManager, never()).setAssignedRoles(eq(forUser(collaboratorId)), eq(projectResource), any());
    }

    @Test
    public void shouldRemoveCollaboratorWhenAnotherEntryIsOnlyConfirmedAbsentNotAFailure() {
        UserId collaboratorId = MockingUtils.mockUserId();
        givenCurrentAccess(collaboratorId, CAN_VIEW.getRoleId());

        PersonId unresolvedPersonId = PersonId.get("unresolved-person");
        when(userLookup.getUserByUserIdOrEmail(unresolvedPersonId.getId())).thenReturn(Optional.empty());

        ProjectSharingSettings settings = new ProjectSharingSettings(
                projectId,
                Optional.empty(),
                List.of(new SharingSetting(unresolvedPersonId, SharingPermission.EDIT)));

        // A confirmed-absent result carries no identity ambiguity, so it must not gate the removal
        // of an unrelated, legitimately-dropped collaborator - the call must not throw either.
        assertDoesNotThrow(() -> manager.setProjectSharingSettings(actingUserId, settings));

        verify(accessManager).setAssignedRoles(eq(forUser(collaboratorId)), eq(projectResource), eq(Set.of()));
    }

    @Test
    public void shouldNotTouchUnrelatedNoOpCollaboratorWhenAnotherEntryFails() {
        UserId collaboratorId = MockingUtils.mockUserId();
        PersonId collaboratorPersonId = PersonId.of(collaboratorId);
        when(userLookup.getUserByUserIdOrEmail(collaboratorPersonId.getId())).thenReturn(Optional.of(collaboratorId));
        givenCurrentAccess(collaboratorId, CAN_VIEW.getRoleId());

        PersonId failingPersonId = PersonId.get("failing-person");
        UserLookupException lookupException = new UserLookupException("boom", new RuntimeException("cause"));
        when(userLookup.getUserByUserIdOrEmail(failingPersonId.getId())).thenThrow(lookupException);

        ProjectSharingSettings settings = new ProjectSharingSettings(
                projectId,
                Optional.empty(),
                List.of(new SharingSetting(collaboratorPersonId, SharingPermission.VIEW),
                        new SharingSetting(failingPersonId, SharingPermission.EDIT)));

        RuntimeException thrown = assertThrows(RuntimeException.class,
                                                () -> manager.setProjectSharingSettings(actingUserId, settings));
        assertThat(thrown, is(lookupException));

        // The collaborator's submitted permission matches their current one (a no-op) and a wholly
        // unrelated entry failed - neither reason permits touching their access.
        verify(accessManager, never()).setAssignedRoles(eq(forUser(collaboratorId)), eq(projectResource), any());
    }

    @Test
    public void shouldSkipSetAssignedRolesForLinkSharingWhenSubmittedPermissionMatchesCurrent() {
        when(accessManager.getAssignedRoles(forAnySignedInUser(), projectResource))
                .thenReturn(Set.of(CAN_EDIT.getRoleId()));

        ProjectSharingSettings settings = new ProjectSharingSettings(
                projectId,
                Optional.of(SharingPermission.EDIT),
                List.of());

        assertDoesNotThrow(() -> manager.setProjectSharingSettings(actingUserId, settings));

        verify(accessManager, never()).setAssignedRoles(eq(forAnySignedInUser()), eq(projectResource), any());
    }

    @Test
    public void shouldPreserveNonSharingRoleWhenCollaboratorsSharingRoleIsUpdated() {
        UserId collaboratorId = MockingUtils.mockUserId();
        PersonId collaboratorPersonId = PersonId.of(collaboratorId);
        when(userLookup.getUserByUserIdOrEmail(collaboratorPersonId.getId())).thenReturn(Optional.of(collaboratorId));

        givenCurrentAccess(collaboratorId, new RoleId("PreExistingRole"), CAN_VIEW.getRoleId());

        ProjectSharingSettings settings = new ProjectSharingSettings(
                projectId,
                Optional.empty(),
                List.of(new SharingSetting(collaboratorPersonId, SharingPermission.EDIT)));

        manager.setProjectSharingSettings(actingUserId, settings);

        Set<RoleId> expectedRoles = new HashSet<>(Set.of(new RoleId("PreExistingRole")));
        expectedRoles.addAll(Roles.fromSharingPermission(SharingPermission.EDIT));
        verify(accessManager).setAssignedRoles(eq(forUser(collaboratorId)), eq(projectResource), eq(expectedRoles));
    }

    @Test
    public void shouldPreserveNonSharingRoleWhenCollaboratorsSharingRoleIsRemoved() {
        UserId collaboratorId = MockingUtils.mockUserId();
        givenCurrentAccess(collaboratorId, new RoleId("PreExistingRole"), CAN_VIEW.getRoleId());

        ProjectSharingSettings settings = new ProjectSharingSettings(
                projectId,
                Optional.empty(),
                List.of());

        assertDoesNotThrow(() -> manager.setProjectSharingSettings(actingUserId, settings));

        verify(accessManager).setAssignedRoles(eq(forUser(collaboratorId)),
                                                eq(projectResource),
                                                eq(Set.of(new RoleId("PreExistingRole"))));
    }

    @Test
    public void shouldTreatDuplicateMatchFailureAsBlockingRemovalsJustLikeALookupFailure() {
        UserId collaboratorId = MockingUtils.mockUserId();
        givenCurrentAccess(collaboratorId, CAN_VIEW.getRoleId());

        PersonId duplicatePersonId = PersonId.get("duplicate-person");
        RuntimeException duplicateUserException = new RuntimeException("Duplicated user with username duplicate-person");
        when(userLookup.getUserByUserIdOrEmail(duplicatePersonId.getId())).thenThrow(duplicateUserException);

        ProjectSharingSettings settings = new ProjectSharingSettings(
                projectId,
                Optional.empty(),
                List.of(new SharingSetting(duplicatePersonId, SharingPermission.EDIT)));

        RuntimeException thrown = assertThrows(RuntimeException.class,
                                                () -> manager.setProjectSharingSettings(actingUserId, settings));
        assertThat(thrown, is(duplicateUserException));

        // The duplicate-match RuntimeException is a lookup failure for removal-gating purposes just
        // as much as a UserLookupException - it must block the otherwise-legitimate removal below.
        verify(accessManager, never()).setAssignedRoles(eq(forUser(collaboratorId)), eq(projectResource), any());
    }

    @Test
    public void shouldPreserveNonSharingRoleForLinkSharingWhenPermissionChanges() {
        when(accessManager.getAssignedRoles(forAnySignedInUser(), projectResource))
                .thenReturn(Set.of(new RoleId("PreExistingLinkRole"), CAN_VIEW.getRoleId()));

        ProjectSharingSettings settings = new ProjectSharingSettings(
                projectId,
                Optional.of(SharingPermission.EDIT),
                List.of());

        manager.setProjectSharingSettings(actingUserId, settings);

        // Link sharing gets the same non-sharing-role preservation as every other subject.
        Set<RoleId> expectedRoles = new HashSet<>(Set.of(new RoleId("PreExistingLinkRole")));
        expectedRoles.addAll(Roles.fromSharingPermission(SharingPermission.EDIT));
        verify(accessManager).setAssignedRoles(eq(forAnySignedInUser()), eq(projectResource), eq(expectedRoles));
    }

    @Test
    public void shouldNotTouchSubjectHoldingOnlyNonSharingRoleWhenAbsentFromSubmittedSettings() {
        UserId subjectId = MockingUtils.mockUserId();
        givenCurrentAccess(subjectId, new RoleId("PreExistingRole"));

        ProjectSharingSettings settings = new ProjectSharingSettings(
                projectId,
                Optional.empty(),
                List.of());

        assertDoesNotThrow(() -> manager.setProjectSharingSettings(actingUserId, settings));

        // This subject never held a sharing role to begin with (only an unrelated one) - they were
        // never really "in the sharing list", so their absence from the submitted settings must not
        // trigger any call at all for them.
        verify(accessManager, never()).setAssignedRoles(eq(forUser(subjectId)), eq(projectResource), any());
    }

    @Test
    public void shouldIgnoreEntryThatResolvesToTheGuestUser() {
        PersonId guestPersonId = PersonId.get("guest-alias");
        when(userLookup.getUserByUserIdOrEmail(guestPersonId.getId())).thenReturn(Optional.of(UserId.getGuest()));

        ProjectSharingSettings settings = new ProjectSharingSettings(
                projectId,
                Optional.empty(),
                List.of(new SharingSetting(guestPersonId, SharingPermission.EDIT)));

        assertDoesNotThrow(() -> manager.setProjectSharingSettings(actingUserId, settings));

        // The guest user is never treated as an individual collaborator - resolving to it must not
        // result in any role-assignment call at all.
        verify(accessManager, never()).setAssignedRoles(eq(forUser(UserId.getGuest())), eq(projectResource), any());
    }

    @Test
    public void shouldApplyOnlyOneUpdateWhenTwoSubmittedEntriesResolveToTheSameUser() {
        UserId collaboratorId = MockingUtils.mockUserId();
        PersonId emailPersonId = PersonId.get("collaborator@example.com");
        PersonId usernamePersonId = PersonId.get("collaborator-username");
        when(userLookup.getUserByUserIdOrEmail(emailPersonId.getId())).thenReturn(Optional.of(collaboratorId));
        when(userLookup.getUserByUserIdOrEmail(usernamePersonId.getId())).thenReturn(Optional.of(collaboratorId));

        ProjectSharingSettings settings = new ProjectSharingSettings(
                projectId,
                Optional.empty(),
                List.of(new SharingSetting(emailPersonId, SharingPermission.VIEW),
                        new SharingSetting(usernamePersonId, SharingPermission.EDIT)));

        assertDoesNotThrow(() -> manager.setProjectSharingSettings(actingUserId, settings));

        // Both submitted entries resolved to the same real person - only one update call is made
        // for them, not two (whichever permission was processed last wins; iteration order over the
        // submitted list is not guaranteed, so this only asserts the call count).
        verify(accessManager, times(1)).setAssignedRoles(eq(forUser(collaboratorId)), eq(projectResource), any());
    }

    @Test
    public void shouldAttachLookupFailureAsSuppressedWhenALaterAccessManagerWriteThrows() {
        PersonId failingPersonId = PersonId.get("failing-person");
        UserLookupException lookupException = new UserLookupException("boom", new RuntimeException("cause"));
        when(userLookup.getUserByUserIdOrEmail(failingPersonId.getId())).thenThrow(lookupException);

        UserId collaboratorId = MockingUtils.mockUserId();
        PersonId collaboratorPersonId = PersonId.of(collaboratorId);
        when(userLookup.getUserByUserIdOrEmail(collaboratorPersonId.getId())).thenReturn(Optional.of(collaboratorId));

        RuntimeException accessManagerFailure = new RuntimeException("access manager RPC failure");
        doThrow(accessManagerFailure).when(accessManager)
                                      .setAssignedRoles(eq(forUser(collaboratorId)), eq(projectResource), any());

        ProjectSharingSettings settings = new ProjectSharingSettings(
                projectId,
                Optional.empty(),
                List.of(new SharingSetting(failingPersonId, SharingPermission.EDIT),
                        new SharingSetting(collaboratorPersonId, SharingPermission.VIEW)));

        RuntimeException thrown = assertThrows(RuntimeException.class,
                                                () -> manager.setProjectSharingSettings(actingUserId, settings));

        // The AccessManager write failure is what's thrown (it happened after the lookup failure
        // was already recorded), but the earlier lookup failure must not be silently lost.
        assertThat(thrown, is(accessManagerFailure));
        assertThat(thrown.getSuppressed().length, is(1));
        assertThat(thrown.getSuppressed()[0], is(lookupException));
    }
}
