package edu.stanford.bmir.protege.web.server.event;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/12/2012
 */
public interface OntologyFrameChangedEventHandler {

    void ontologyFrameChanged(OntologyFrameChangedEvent event);
}
