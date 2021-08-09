package edu.stanford.protege.webprotege.event;


import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.user.UserId;
import org.semanticweb.owlapi.model.OWLDatatype;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/12/2012
 */
public class DatatypeFrameChangedEvent extends EntityFrameChangedEvent<OWLDatatype, DatatypeFrameChangedEventHandler> {

    public DatatypeFrameChangedEvent(OWLDatatype entity, ProjectId projectId, UserId userId) {
        super(entity, projectId, userId);
    }

    private DatatypeFrameChangedEvent() {
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
    protected void dispatch(DatatypeFrameChangedEventHandler handler) {
        handler.datatypeFrameChanged(this);
    }
}
