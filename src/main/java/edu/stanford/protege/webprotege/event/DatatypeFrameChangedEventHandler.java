package edu.stanford.protege.webprotege.event;



/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/12/2012
 */
public interface DatatypeFrameChangedEventHandler extends EventHandler {

    void datatypeFrameChanged(DatatypeFrameChangedEvent event);
}
