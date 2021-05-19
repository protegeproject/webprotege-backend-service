package edu.stanford.protege.webprotege.event;



/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/03/2013
 */
public interface EntityDeprecatedChangedHandler extends EventHandler {

    void handleEntityDeprecatedChangedEvent(EntityDeprecatedChangedEvent evt);
}
