package edu.stanford.bmir.protege.web.server.event;


import edu.stanford.bmir.protege.web.server.project.ProjectId;
import edu.stanford.bmir.protege.web.server.user.UserId;
import org.semanticweb.owlapi.model.OWLDataProperty;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/12/2012
 */
public class DataPropertyFrameChangedEvent extends EntityFrameChangedEvent<OWLDataProperty, DataPropertyFrameChangedEventHandler> {

    public DataPropertyFrameChangedEvent(OWLDataProperty entity, ProjectId projectId, UserId userId) {
        super(entity, projectId, userId);
    }

    private DataPropertyFrameChangedEvent() {
    }

    /**
     * Implemented by subclasses to to invoke their handlers in a type safe
     * manner. Intended to be called by {@link com.google.web.bindery.event.shared.EventBus#fireEvent(
     *com.google.web.bindery.event.shared.Event)} or
     * {@link com.google.web.bindery.event.shared.EventBus#fireEventFromSource(com.google.web.bindery.event.shared.Event,
     * Object)}.
     * @param handler handler
     * @see com.google.web.bindery.event.shared.EventBus#dispatchEvent(com.google.web.bindery.event.shared.Event,
     *      Object)
     */
    @Override
    protected void dispatch(DataPropertyFrameChangedEventHandler handler) {
        handler.dataPropertyFrameChanged(this);
    }
}
