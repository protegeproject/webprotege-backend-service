package edu.stanford.bmir.protege.web.server.issues;



import edu.stanford.bmir.protege.web.server.event.ProjectEvent;
import edu.stanford.bmir.protege.web.server.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Oct 2016
 */
public class DiscussionThreadStatusChangedEvent extends ProjectEvent<DiscussionThreadStatusChangedHandler> {

    private ProjectId projectId;

    private ThreadId threadId;

    @Nullable
    private OWLEntity entity;

    private int openCommentsCountForEntity;

    private Status status;

    public DiscussionThreadStatusChangedEvent(@Nonnull ProjectId projectId,
                                              @Nonnull ThreadId threadId,
                                              @Nonnull Optional<OWLEntity> entity,
                                              int openCommentsCountForEntity,
                                              @Nonnull Status status) {
        super(projectId);
        this.projectId = checkNotNull(projectId);
        this.threadId = checkNotNull(threadId);
        this.entity = checkNotNull(entity).orElse(null);
        this.openCommentsCountForEntity = openCommentsCountForEntity;
        this.status = checkNotNull(status);
    }


    private DiscussionThreadStatusChangedEvent() {
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    public ThreadId getThreadId() {
        return threadId;
    }

    @Nonnull
    public Optional<OWLEntity> getEntity() {
        return Optional.ofNullable(entity);
    }

    /**
     * Gets the open threads for the entity that this thread pertains to.
     * @return The number of open threads.  Undefined if the entity for this thread is not set.
     */
    public int getOpenCommentsCountForEntity() {
        return openCommentsCountForEntity;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    protected void dispatch(DiscussionThreadStatusChangedHandler handler) {
        handler.handleDiscussionThreadStatusChanged(this);
    }


    @Override
    public String toString() {
        return toStringHelper("DiscussionThreadStatusChangedEvent" )
                .addValue(threadId)
                .addValue(entity)
                .addValue(status)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DiscussionThreadStatusChangedEvent)) {
            return false;
        }
        DiscussionThreadStatusChangedEvent that = (DiscussionThreadStatusChangedEvent) o;
        return openCommentsCountForEntity == that.openCommentsCountForEntity && Objects.equals(projectId,
                                                                                               that.projectId) && Objects
                .equals(threadId, that.threadId) && Objects.equals(entity, that.entity) && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId, threadId, entity, openCommentsCountForEntity, status);
    }
}
