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
import java.time.Instant;
import java.util.*;

import static edu.stanford.protege.webprotege.authorization.Subject.forAnySignedInUser;
import static edu.stanford.protege.webprotege.authorization.Subject.forUser;
import static java.util.Collections.emptySet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 05/02/15
 */
public class ProjectSharingSettingsManagerImpl implements ProjectSharingSettingsManager {

    private final Logger logger = LoggerFactory.getLogger(ProjectSharingSettingsManagerImpl.class);

    private final AccessManager accessManager;

    private final UserDetailsManager userLookup;

    private final PendingSharingInvitationRepository pendingInvitationRepository;

    private final SharingInvitationEmailer invitationEmailer;

    @Inject
    public ProjectSharingSettingsManagerImpl(AccessManager accessManager,
                                             UserDetailsManager userLookup,
                                             PendingSharingInvitationRepository pendingInvitationRepository,
                                             SharingInvitationEmailer invitationEmailer) {
        this.accessManager = accessManager;
        this.userLookup = userLookup;
        this.pendingInvitationRepository = pendingInvitationRepository;
        this.invitationEmailer = invitationEmailer;
    }

    @Override
    public ProjectSharingSettings getProjectSharingSettings(ProjectId projectId) {
        List<SharingSetting> sharingSettings = new ArrayList<>();
        ProjectResource projectResource = new ProjectResource(projectId);
        Collection<Subject> subjects = accessManager.getSubjectsWithAccessToResource(projectResource);
        // The keys of every active collaborator (their user id, plus their known local email) so that
        // a pending invitation that has since become an active collaborator is not shown twice - the
        // active entry wins on a key collision.
        Set<String> activeKeys = new HashSet<>();
        subjects.stream()
                .filter(s -> !s.isGuest())
                .filter(s -> s.getUserName().isPresent())
                .map(s -> UserId.valueOf(s.getUserName().get()))
                .forEach(u -> {
                    Collection<RoleId> roles = accessManager.getAssignedRoles(forUser(u), projectResource);
                    Roles.toSharingPermission(roles).ifPresent(p -> {
                        sharingSettings.add(new SharingSetting(PersonId.of(u), p));
                        // Only a subject who produces a visible collaborator row suppresses a matching
                        // pending row (via their id and their best-effort local email). A subject
                        // holding only non-sharing roles must not suppress it - doing so would both
                        // hide the pending row and let the next save's reconciliation delete the
                        // owner's pending intent.
                        activeKeys.add(PendingSharingInvitation.normalizeKey(u.id()));
                        addKnownEmailKey(activeKeys, u);
                    });
                });
        // A read failure here must propagate and fail the load: returning a partial list would let a
        // subsequent save's reconciliation mistake the unseen pending invitations for removals.
        for (PendingSharingInvitation invitation : pendingInvitationRepository.findByProjectId(projectId)) {
            if (!activeKeys.contains(invitation.personKey())) {
                sharingSettings.add(new SharingSetting(PersonId.get(invitation.personId()),
                                                       invitation.sharingPermission()));
            }
        }
        Collection<RoleId> signedInUserRoles = accessManager.getAssignedRoles(forAnySignedInUser(), projectResource);
        Optional<SharingPermission> linkSharing = Roles.toSharingPermission(signedInUserRoles);
        return new ProjectSharingSettings(projectId, linkSharing, sharingSettings);
    }

    private void addKnownEmailKey(Set<String> activeKeys, UserId userId) {
        try {
            userLookup.getEmail(userId)
                      .map(PendingSharingInvitation::normalizeKey)
                      .filter(key -> !key.isEmpty())
                      .ifPresent(activeKeys::add);
        } catch (RuntimeException e) {
            // Best effort only - if we cannot fetch the email we simply fall back to showing the
            // pending row, which is harmless.
            logger.warn("Could not look up the email for active collaborator {} while merging pending " +
                        "sharing invitations: {}", userId, e.toString());
        }
    }

    @Override
    public void setProjectSharingSettings(@Nonnull UserId actingUserId, @Nonnull ProjectSharingSettings settings) {
        ProjectId projectId = settings.getProjectId();
        ProjectResource projectResource = new ProjectResource(projectId);

        // Preserve the submitted order (keeping the first occurrence of any exactly-duplicated
        // PersonId) so that de-duplicating confirmed-absent entries by normalized key is
        // deterministic: the first-submitted entry wins on a normalized-key collision.
        Map<PersonId, SharingSetting> map = new LinkedHashMap<>();
        for (SharingSetting setting : settings.getSharingSettings()) {
            map.putIfAbsent(setting.getPersonId(), setting);
        }

        // Phase 1: classify every submitted entry as resolved / confirmed-absent / failed. A lookup
        // failure (an infra error, or the duplicate-match RuntimeException) for one entry must never
        // affect any other person's access - keep processing the rest, remember the first failure
        // (with later ones suppressed onto it), and rethrow it at the end. A confirmed-absent
        // (Optional.empty()) result is not a failure: it carries no identity ambiguity, so it must
        // not gate the removal step below, and it becomes a pending invitation.
        //
        // This phase performs NO writes - not to the access manager, and not to the pending-invitation
        // store. All store writes and email dispatch happen only after Phases 2-5 succeed, so a phase
        // failure leaves the pending store untouched and a retried save regenerates the same
        // first-time-invitation email signal.
        Map<UserId, SharingPermission> desiredByUser = new HashMap<>();
        Map<String, PendingSharingInvitation> confirmedAbsentByKey = new LinkedHashMap<>();
        Set<String> failedKeys = new HashSet<>();
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
                String failedKey = PendingSharingInvitation.normalizeKey(personId.getId());
                if (!failedKey.isEmpty()) {
                    failedKeys.add(failedKey);
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
                collectConfirmedAbsent(projectId, actingUserId, confirmedAbsentByKey, setting);
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

        // The access-manager phases succeeded. Only now update the pending-invitation store and
        // dispatch any first-time-invitation emails; neither step may fail the save, and neither may
        // swallow the recorded lookup failure that must still be rethrown below.
        applyPendingInvitations(projectId, confirmedAbsentByKey, failedKeys);

        if (lookupFailure != null) {
            throw lookupFailure;
        }
    }

    private void collectConfirmedAbsent(ProjectId projectId,
                                        UserId actingUserId,
                                        Map<String, PendingSharingInvitation> confirmedAbsentByKey,
                                        SharingSetting setting) {
        PersonId personId = setting.getPersonId();
        String key = PendingSharingInvitation.normalizeKey(personId.getId());
        if (key.isEmpty()) {
            logger.warn("Ignoring a project sharing setting whose person id '{}' normalizes to an empty " +
                        "key for project {}.", personId.getId(), projectId);
            return;
        }
        if (confirmedAbsentByKey.containsKey(key)) {
            logger.warn("More than one submitted sharing setting for project {} resolves to the same " +
                        "pending-invitation key ('{}') - only the first one will be stored.", projectId, key);
            return;
        }
        confirmedAbsentByKey.put(key, new PendingSharingInvitation(projectId,
                                                                   personId.getId(),
                                                                   personId.getId(),
                                                                   setting.getSharingPermission(),
                                                                   actingUserId,
                                                                   Instant.now()));
    }

    /**
     * Upserts the confirmed-absent invitations, reconciles the project's pending invitations against
     * what was submitted, and dispatches an email for each brand-new email-shaped invitation. Runs
     * only after the access-manager phases have succeeded, and never throws - a store or mail failure
     * here must not fail the save.
     */
    private void applyPendingInvitations(ProjectId projectId,
                                         Map<String, PendingSharingInvitation> confirmedAbsentByKey,
                                         Set<String> failedKeys) {
        // Reconcile FIRST, in its own guard: it depends only on the keys computed in Phase 1, so a
        // later upsert failure must never be able to skip the removal of invitations the owner
        // dropped or that now resolve to a real user. Keep only the invitations we just (re)confirmed
        // as absent, plus those whose lookup failed this call (we cannot safely decide about them).
        try {
            Set<String> keepKeys = new HashSet<>(confirmedAbsentByKey.keySet());
            keepKeys.addAll(failedKeys);
            pendingInvitationRepository.deleteByProjectIdWherePersonKeyNotIn(projectId, keepKeys);
        }
        catch (RuntimeException e) {
            logger.error("Could not reconcile the pending sharing invitations for project {}: {}",
                         projectId, e.toString());
        }
        // Then upsert each confirmed-absent invitation independently, so one failed upsert does not
        // skip the others; collect the ones that were freshly inserted so only they are emailed.
        List<PendingSharingInvitation> freshInserts = new ArrayList<>();
        for (PendingSharingInvitation invitation : confirmedAbsentByKey.values()) {
            try {
                if (pendingInvitationRepository.upsert(invitation)) {
                    freshInserts.add(invitation);
                }
            }
            catch (RuntimeException e) {
                logger.error("Could not store the pending sharing invitation for '{}' in project {}: {}",
                             invitation.personId(), projectId, e.toString());
            }
        }
        // Email only brand-new invitations (never a re-save), and only when the id is email-shaped.
        try {
            for (PendingSharingInvitation invitation : freshInserts) {
                if (PendingSharingInvitation.isEmailShaped(invitation.personKey())) {
                    invitationEmailer.sendInvitationEmail(projectId, invitation.personId(), invitation.invitedBy());
                }
            }
        }
        catch (RuntimeException e) {
            logger.error("Could not dispatch sharing invitation emails for project {}: {}",
                         projectId, e.toString());
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
