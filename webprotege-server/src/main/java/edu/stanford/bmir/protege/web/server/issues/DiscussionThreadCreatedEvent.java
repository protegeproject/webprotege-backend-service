package edu.stanford.bmir.protege.web.server.issues;



import edu.stanford.bmir.protege.web.server.event.ProjectEvent;
import edu.stanford.bmir.protege.web.server.project.HasProjectId;

import java.util.Objects;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Oct 2016
 */
public class DiscussionThreadCreatedEvent extends ProjectEvent<DiscussionThreadCreatedHandler> implements HasProjectId {

    private EntityDiscussionThread thread;

    public DiscussionThreadCreatedEvent(EntityDiscussionThread thread) {
        super(thread.getProjectId());
        this.thread = thread;
    }


    private DiscussionThreadCreatedEvent() {
    }

    public EntityDiscussionThread getThread() {
        return thread;
    }

    @Override
    protected void dispatch(DiscussionThreadCreatedHandler handler) {
        handler.handleDiscussionThreadCreated(this);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DiscussionThreadCreatedEvent)) {
            return false;
        }
        DiscussionThreadCreatedEvent that = (DiscussionThreadCreatedEvent) o;
        return Objects.equals(thread, that.thread) && Objects.equals(getProjectId(), ((DiscussionThreadCreatedEvent) o).getProjectId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(thread, getProjectId());
    }
}
