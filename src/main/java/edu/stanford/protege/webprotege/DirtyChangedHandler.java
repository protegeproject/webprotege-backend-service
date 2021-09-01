package edu.stanford.protege.webprotege;



/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/03/2013
 */
public interface DirtyChangedHandler {

    void handleDirtyChanged(DirtyChangedEvent event);
}
