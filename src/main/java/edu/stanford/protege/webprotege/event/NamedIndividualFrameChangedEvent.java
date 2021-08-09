package edu.stanford.protege.webprotege.event;


import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/12/2012
 */
public class NamedIndividualFrameChangedEvent extends EntityFrameChangedEvent<OWLNamedIndividual, NamedIndividualFrameChangedEventHandler> {

    public NamedIndividualFrameChangedEvent(OWLNamedIndividual entity, ProjectId projectId, UserId userId) {
        super(entity, projectId, userId);
    }

    private NamedIndividualFrameChangedEvent() {
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
    protected void dispatch(NamedIndividualFrameChangedEventHandler handler) {
        handler.namedIndividualFrameChanged(this);
    }
}
