package edu.stanford.protege.webprotege.sharing;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.persistence.Repository;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

/**
 * Stores {@link PendingSharingInvitation}s for people who were shared with before they had a
 * WebProtege account.
 */
public interface PendingSharingInvitationRepository extends Repository {

    /**
     * Creates or replaces the invitation identified by its project and person key.  An existing
     * invitation's original {@code createdAt} and {@code invitedBy} are preserved when it is
     * replaced.
     *
     * @return {@code true} if this call inserted a brand new invitation, {@code false} if it
     * replaced an existing one.
     */
    boolean upsert(@Nonnull PendingSharingInvitation invitation);

    @Nonnull
    List<PendingSharingInvitation> findByPersonKeys(@Nonnull Collection<String> personKeys);

    @Nonnull
    List<PendingSharingInvitation> findByProjectId(@Nonnull ProjectId projectId);

    /**
     * Deletes every invitation for the specified project whose person key is not in
     * {@code personKeysToKeep}.
     */
    void deleteByProjectIdWherePersonKeyNotIn(@Nonnull ProjectId projectId,
                                              @Nonnull Collection<String> personKeysToKeep);

    /**
     * Deletes the invitation identified by its project and person key.
     *
     * @return the number of invitations deleted (0 or 1), so a caller can treat a positive count as
     * an atomic claim on the invitation.
     */
    long delete(@Nonnull ProjectId projectId, @Nonnull String personKey);
}
