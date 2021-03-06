package edu.stanford.protege.webprotege.webhook;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 May 2017
 */
public class ProjectChangedWebhookPayload {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final UserId userId;

    private final long revisionNumber;

    private final long timestamp;

    public ProjectChangedWebhookPayload(@Nonnull ProjectId projectId,
                                        @Nonnull UserId userId,
                                        long revisionNumber,
                                        long timestamp) {
        this.projectId = checkNotNull(projectId);
        this.userId = checkNotNull(userId);
        this.revisionNumber = revisionNumber;
        this.timestamp = timestamp;
    }

    @Nonnull
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public UserId getUserId() {
        return userId;
    }

    public long getRevisionNumber() {
        return revisionNumber;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
