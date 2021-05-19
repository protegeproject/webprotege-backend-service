package edu.stanford.protege.webprotege.event;



/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/12/2012
 */
public interface NamedIndividualFrameChangedEventHandler extends EventHandler {

    void namedIndividualFrameChanged(NamedIndividualFrameChangedEvent event);
}
