package edu.stanford.protege.webprotege.sharing;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public interface ProjectSharingSettingsManager {

    /**
     * Applies the submitted sharing settings as a diff against the project's current sharing state,
     * rather than wiping and re-granting every subject's access. Each submitted person-level entry
     * is first resolved to a {@link edu.stanford.protege.webprotege.common.UserId}; only entries
     * that resolve and would actually change a subject's sharing role result in a call to update
     * that subject's roles, and each such call - including for link sharing, which has no lookup of
     * its own to resolve - preserves any non-sharing roles the subject already holds (e.g.
     * {@code PROJECT_DOWNLOADER}, {@code LAYOUT_EDITOR}). A resolved entry that turns out to be the
     * guest user is ignored - the guest user is never treated as an individual collaborator.
     * <p>
     * A current collaborator who is absent from {@code projectSharingSettings} has their sharing
     * role removed (again preserving non-sharing roles) <strong>unless</strong> any entry in this
     * call failed to resolve (threw {@link edu.stanford.protege.webprotege.user.UserLookupException}
     * or the duplicate-match {@link RuntimeException}) - a lookup failure anywhere in the batch
     * structurally cannot be mapped to a specific person, so it must never be allowed to imply
     * anyone's removal. A confirmed-absent lookup result (i.e. no such user exists) is not a failure
     * and does not block removals elsewhere in the same call.
     *
     * @param actingUserId The user making this change. This user is never a removal candidate -
     *                     their access is left completely untouched unless their own entry is
     *                     present in {@code projectSharingSettings} and resolves, in which case it
     *                     is applied like any other entry. This guarantees a user can never lock
     *                     themselves out of a project they are actively editing sharing settings
     *                     for, even if their own lookup fails or they are simply omitted.
     * @throws RuntimeException the recorded lookup failure (if any occurred while resolving
     *                     submitted entries), rethrown after all other entries and removals have
     *                     been processed. If applying a change afterwards itself fails (e.g. an
     *                     unrelated {@code AccessManager} failure), that failure is thrown instead,
     *                     with any already-recorded lookup failure attached to it as a suppressed
     *                     exception rather than lost.
     */
    void setProjectSharingSettings(@Nonnull UserId actingUserId, @Nonnull ProjectSharingSettings projectSharingSettings);

    ProjectSharingSettings getProjectSharingSettings(ProjectId projectId);
}
