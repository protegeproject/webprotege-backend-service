package edu.stanford.protege.webprotege.issues;



import edu.stanford.protege.webprotege.event.ProjectEvent;
import edu.stanford.protege.webprotege.project.HasProjectId;
import edu.stanford.protege.webprotege.project.ProjectId;

import javax.annotation.Nonnull;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Oct 2016
 */
public class CommentUpdatedEvent extends ProjectEvent<CommentUpdatedHandler> implements HasProjectId {

    private ProjectId projectId;

    private ThreadId threadId;

    private Comment comment;

    public CommentUpdatedEvent(@Nonnull ProjectId projectId,
                               @Nonnull ThreadId threadId,
                               @Nonnull Comment comment) {
        super(projectId);
        this.projectId = checkNotNull(projectId);
        this.threadId = checkNotNull(threadId);
        this.comment = checkNotNull(comment);
    }


    private CommentUpdatedEvent() {
    }

    @Override
    protected void dispatch(CommentUpdatedHandler handler) {
        handler.handleCommentUpdated(this);
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    public ThreadId getThreadId() {
        return threadId;
    }

    public Comment getComment() {
        return comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommentUpdatedEvent)) {
            return false;
        }
        CommentUpdatedEvent that = (CommentUpdatedEvent) o;
        return Objects.equals(projectId, that.projectId) && Objects.equals(threadId, that.threadId) && Objects.equals(
                comment,
                that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId, threadId, comment);
    }
}
