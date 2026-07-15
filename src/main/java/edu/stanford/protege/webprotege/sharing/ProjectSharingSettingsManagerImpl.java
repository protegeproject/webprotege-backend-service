package edu.stanford.protege.webprotege.sharing;

import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.authorization.ProjectResource;
import edu.stanford.protege.webprotege.authorization.RoleId;
import edu.stanford.protege.webprotege.authorization.Subject;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.user.UserDetailsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.*;

import static edu.stanford.protege.webprotege.authorization.Subject.forAnySignedInUser;
import static edu.stanford.protege.webprotege.authorization.Subject.forUser;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toMap;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 05/02/15
 */
public class ProjectSharingSettingsManagerImpl implements ProjectSharingSettingsManager {

    private final Logger logger = LoggerFactory.getLogger(ProjectSharingSettingsManagerImpl.class);

    private final AccessManager accessManager;

    private final UserDetailsManager userLookup;

    @Inject
    public ProjectSharingSettingsManagerImpl(AccessManager accessManager,
                                             UserDetailsManager userLookup) {
        this.accessManager = accessManager;
        this.userLookup = userLookup;
    }

    @Override
    public ProjectSharingSettings getProjectSharingSettings(ProjectId projectId) {
        List<SharingSetting> sharingSettings = new ArrayList<>();
        ProjectResource projectResource = new ProjectResource(projectId);
        Collection<Subject> subjects = accessManager.getSubjectsWithAccessToResource(projectResource);
        subjects.stream()
                .filter(s -> !s.isGuest())
                .filter(s -> s.getUserName().isPresent())
                .map(s -> UserId.valueOf(s.getUserName().get()))
                .forEach(u -> {
                    Collection<RoleId> roles = accessManager.getAssignedRoles(forUser(u), projectResource);
                    Roles.toSharingPermission(roles).ifPresent(
                            p -> sharingSettings.add(new SharingSetting(PersonId.of(u), p)));

                });
        Collection<RoleId> signedInUserRoles = accessManager.getAssignedRoles(forAnySignedInUser(), projectResource);
        Optional<SharingPermission> linkSharing = Roles.toSharingPermission(signedInUserRoles);
        return new ProjectSharingSettings(projectId, linkSharing, sharingSettings);
    }


    @Override
    public void setProjectSharingSettings(@Nonnull UserId actingUserId, @Nonnull ProjectSharingSettings settings) {
        ProjectId projectId = settings.getProjectId();
        ProjectResource projectResource = new ProjectResource(projectId);

        // Capture the acting user's roles before making any changes. The
        // person-lookup below is a best-effort, search-based existence
        // check that can fail even for a real, currently signed-in user
        // (transient RPC failure, timeout, indexing gaps) - if that
        // happens for the acting user's own entry, or they are simply not
        // included in the submitted settings, we still must not leave them
        // without the access they had a moment ago. Losing your own access
        // to a project you are actively editing sharing settings for is a
        // lockout with no way back in short of direct DB intervention.
        Collection<RoleId> actingUserRolesBeforeChange =
                accessManager.getAssignedRoles(forUser(actingUserId), projectResource);

        // Remove existing assignments
        accessManager.getSubjectsWithAccessToResource(projectResource)
                .forEach(subject -> accessManager.setAssignedRoles(subject, projectResource, Collections.emptySet()));

        Map<PersonId, SharingSetting> map = settings.getSharingSettings().stream()
                                                    .collect(toMap(SharingSetting::getPersonId, s -> s, (s1, s2) -> s1));
        Optional<SharingPermission> linkSharingPermission = settings.getLinkSharingPermission();
        linkSharingPermission.ifPresent(permission -> {
            Collection<RoleId> roleId = Roles.fromSharingPermission(permission);
            accessManager.setAssignedRoles(forAnySignedInUser(), projectResource, roleId);
        });
        if(!linkSharingPermission.isPresent()) {
            accessManager.setAssignedRoles(forAnySignedInUser(), projectResource, emptySet());
        }
        boolean actingUserRoleWasSet = false;
        RuntimeException lookupFailure = null;
        for (SharingSetting setting : map.values()) {
            PersonId personId = setting.getPersonId();
            Optional<UserId> userId;
            try {
                userId = userLookup.getUserByUserIdOrEmail(personId.getId());
            } catch (RuntimeException e) {
                // The full stack trace for this exception was already logged inside the lookup
                // itself (or, for a duplicate-match, is self-explanatory) - avoid dumping it again here.
                logger.error("Could not look up the user for a project sharing setting (person id '{}', " +
                             "project {}) - their access was not updated: {}",
                             personId.getId(), projectId, e.toString());
                if (lookupFailure == null) {
                    lookupFailure = e;
                } else {
                    lookupFailure.addSuppressed(e);
                }
                continue;
            }
            if (userId.isPresent()) {
                ImmutableSet<RoleId> roles = Roles.fromSharingPermission(setting.getSharingPermission());
                accessManager.setAssignedRoles(forUser(userId.get()),
                                               projectResource,
                                               roles);
                if (userId.get().equals(actingUserId)) {
                    actingUserRoleWasSet = true;
                }
            }
            else {
                logger.warn("Could not resolve the user for a project sharing setting (person id '{}', " +
                            "project {}) - their access was not updated.  An email invitation needs to " +
                            "be sent.", personId.getId(), projectId);
                // TODO
                // We need to send the user an email invitation
            }
        }

        // Safety net: never let this call remove the acting user's own
        // access. If their entry did not resolve above, or they were not
        // part of the submitted settings at all, restore whatever access
        // they had immediately before this call.
        if (!actingUserRoleWasSet && !actingUserRolesBeforeChange.isEmpty()) {
            logger.warn("Restoring user {}'s pre-existing access to project {} because the submitted " +
                        "sharing settings would otherwise have removed it.", actingUserId, projectId);
            try {
                accessManager.setAssignedRoles(forUser(actingUserId), projectResource, new HashSet<>(actingUserRolesBeforeChange));
            } catch (RuntimeException e) {
                if (lookupFailure != null) {
                    e.addSuppressed(lookupFailure);
                }
                throw e;
            }
        }

        if (lookupFailure != null) {
            throw lookupFailure;
        }
    }
}
