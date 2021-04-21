package edu.stanford.bmir.protege.web.shared.event;


import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.OWLObjectProperty;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/12/2012
 */
public class ObjectPropertyFrameChangedEvent extends EntityFrameChangedEvent<OWLObjectProperty, ObjectPropertyFrameChangedEventHandler> {

    public ObjectPropertyFrameChangedEvent(OWLObjectProperty entity, ProjectId projectId, UserId userId) {
        super(entity, projectId, userId);
    }

    private ObjectPropertyFrameChangedEvent() {
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
    protected void dispatch(ObjectPropertyFrameChangedEventHandler handler) {
        handler.objectPropertyFrameChanged(this);
    }
}
