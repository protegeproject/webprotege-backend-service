package edu.stanford.bmir.protege.web.server.issues;



import edu.stanford.bmir.protege.web.server.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.server.event.ProjectEvent;
import edu.stanford.bmir.protege.web.server.project.HasProjectId;
import edu.stanford.bmir.protege.web.server.project.ProjectId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Oct 2016
 */
public class CommentPostedEvent extends ProjectEvent<CommentPostedHandler> implements HasProjectId {

    private ProjectId projectId;

    private ThreadId threadId;

    private Comment comment;

    @Nullable
    private OWLEntityData entity;

    private int commentCountForEntity;

    private int openCommentCountForEntity;

    public CommentPostedEvent(@Nonnull ProjectId projectId,
                              @Nonnull ThreadId threadId,
                              @Nonnull Comment comment,
                              @Nonnull Optional<OWLEntityData> entity,
                              int commentCountForEntity,
                              int openCommentCountForEntity) {
        super(projectId);
        this.projectId = checkNotNull(projectId);
        this.threadId = checkNotNull(threadId);
        this.comment = checkNotNull(comment);
        this.entity = checkNotNull(entity).orElse(null);
        this.commentCountForEntity = commentCountForEntity;
        this.openCommentCountForEntity = openCommentCountForEntity;
    }


    private CommentPostedEvent() {
    }

    @Override
    protected void dispatch(CommentPostedHandler handler) {
        handler.handleCommentPosted(this);
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

    public Optional<OWLEntityData> getEntity() {
        return Optional.ofNullable(entity);
    }

    public int getCommentCountForEntity() {
        return commentCountForEntity;
    }

    public int getOpenCommentCountForEntity() {
        return openCommentCountForEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommentPostedEvent)) {
            return false;
        }
        CommentPostedEvent that = (CommentPostedEvent) o;
        return commentCountForEntity == that.commentCountForEntity && openCommentCountForEntity == that.openCommentCountForEntity && projectId
                .equals(that.projectId) && threadId.equals(that.threadId) && comment.equals(that.comment) && Objects.equals(
                entity,
                that.entity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId, threadId, comment, entity, commentCountForEntity, openCommentCountForEntity);
    }
}
