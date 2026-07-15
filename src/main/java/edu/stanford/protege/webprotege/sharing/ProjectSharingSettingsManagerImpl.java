package edu.stanford.protege.webprotege.sharing;

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

        Map<PersonId, SharingSetting> map = settings.getSharingSettings().stream()
                                                    .collect(toMap(SharingSetting::getPersonId, s -> s, (s1, s2) -> s1));

        // Phase 1: resolve every submitted entry to a UserId first. A lookup failure (an infra
        // error, or the duplicate-match RuntimeException) for one entry must never affect any
        // other person's access - keep processing the rest, remember the first failure (with
        // later ones suppressed onto it), and rethrow it at the end. A confirmed-absent
        // (Optional.empty()) result is not a failure: it carries no identity ambiguity, so it must
        // not gate the removal step below.
        Map<UserId, SharingPermission> desiredByUser = new HashMap<>();
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
                if (userId.get().isGuest()) {
                    logger.warn("Resolved user for a project sharing setting (person id '{}', project {}) is " +
                                "the guest user - ignoring, the guest user is never an individual collaborator.",
                                personId.getId(), projectId);
                }
                else if (desiredByUser.put(userId.get(), setting.getSharingPermission()) != null) {
                    logger.warn("More than one submitted sharing setting resolved to the same user ({}) for " +
                                "project {} - only the last one processed will be applied.", userId.get(), projectId);
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

        // Phases 2-5 are wrapped so that if any of them throws (e.g. an AccessManager RPC failure
        // unrelated to a lookup), an already-recorded lookupFailure is not silently lost - it is
        // attached to the new exception as a suppressed cause, matching how the old #176 safety net
        // protected its own write in the same way.
        try {
            // Phase 2: load each current collaborator's raw role set (never on raw PersonId - the
            // same person can appear as textually different PersonId values across current vs.
            // submitted state). This both drives the diff below and lets us preserve each subject's
            // non-sharing roles across updates and removals.
            Map<UserId, Collection<RoleId>> currentByUser = new HashMap<>();
            accessManager.getSubjectsWithAccessToResource(projectResource).stream()
                         .filter(subject -> !subject.isGuest())
                         .filter(subject -> subject.getUserId().isPresent())
                         .forEach(subject -> currentByUser.put(subject.getUserId().get(),
                                                                accessManager.getAssignedRoles(subject, projectResource)));

            // Phase 3: link sharing - only touch it if the submitted permission actually differs
            // from the current one, preserving any non-sharing roles forAnySignedInUser() holds
            // (e.g. the default LAYOUT_EDITOR grant), exactly like every other subject below.
            Collection<RoleId> currentLinkSharingRoles = accessManager.getAssignedRoles(forAnySignedInUser(), projectResource);
            Optional<SharingPermission> currentLinkSharingPermission = Roles.toSharingPermission(currentLinkSharingRoles);
            Optional<SharingPermission> desiredLinkSharingPermission = settings.getLinkSharingPermission();
            if (!currentLinkSharingPermission.equals(desiredLinkSharingPermission)) {
                Set<RoleId> newLinkSharingRoles = nonSharingSubset(currentLinkSharingRoles);
                desiredLinkSharingPermission.ifPresent(
                        permission -> newLinkSharingRoles.addAll(Roles.fromSharingPermission(permission)));
                accessManager.setAssignedRoles(forAnySignedInUser(), projectResource, newLinkSharingRoles);
            }

            // Phase 4: additions/updates. Skip the call entirely for anything that would not
            // actually change; otherwise preserve the subject's non-sharing roles alongside the new
            // sharing role.
            desiredByUser.forEach((userId, desiredPermission) -> {
                Collection<RoleId> currentRoles = currentByUser.getOrDefault(userId, emptySet());
                if (Roles.toSharingPermission(currentRoles).equals(Optional.of(desiredPermission))) {
                    return;
                }
                Set<RoleId> newRoles = nonSharingSubset(currentRoles);
                newRoles.addAll(Roles.fromSharingPermission(desiredPermission));
                accessManager.setAssignedRoles(forUser(userId), projectResource, newRoles);
            });

            // Phase 5: removals - only when every entry resolved without a lookup failure this
            // call. A failure anywhere in the batch structurally cannot be mapped to a specific
            // person, so it must not be allowed to imply anyone's removal. The acting user is never
            // a removal candidate, unconditionally - this is what makes the old #176 restore-net
            // unnecessary: the acting user can now only ever be updated (their own entry resolving)
            // or left completely untouched (never a removal candidate).
            if (lookupFailure == null) {
                currentByUser.forEach((userId, currentRoles) -> {
                    if (userId.equals(actingUserId) || desiredByUser.containsKey(userId)) {
                        return;
                    }
                    if (Roles.toSharingPermission(currentRoles).isPresent()) {
                        accessManager.setAssignedRoles(forUser(userId), projectResource, nonSharingSubset(currentRoles));
                    }
                });
            }
        }
        catch (RuntimeException e) {
            if (lookupFailure != null) {
                e.addSuppressed(lookupFailure);
            }
            throw e;
        }

        if (lookupFailure != null) {
            throw lookupFailure;
        }
    }

    /**
     * @return a mutable copy of {@code roles} with the sharing-related roles
     * ({@link Roles#SHARING_ROLE_IDS}) removed, i.e. the roles that should be preserved when a
     * subject's sharing permission is updated or removed.
     */
    private static Set<RoleId> nonSharingSubset(Collection<RoleId> roles) {
        Set<RoleId> subset = new HashSet<>(roles);
        subset.removeAll(Roles.SHARING_ROLE_IDS);
        return subset;
    }
}
