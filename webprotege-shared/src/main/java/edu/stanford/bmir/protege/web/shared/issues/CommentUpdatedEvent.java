package edu.stanford.bmir.protege.web.shared.issues;

import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Oct 2016
 */
public class CommentUpdatedEvent extends ProjectEvent<CommentUpdatedHandler> implements HasProjectId {

    public static final transient Event.Type<CommentUpdatedHandler> ON_COMMENT_UPDATED = new Event.Type<>();

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

    @GwtSerializationConstructor
    private CommentUpdatedEvent() {
    }

    @Override
    public Event.Type<CommentUpdatedHandler> getAssociatedType() {
        return ON_COMMENT_UPDATED;
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
