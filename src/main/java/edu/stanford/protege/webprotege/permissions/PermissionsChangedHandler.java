package edu.stanford.protege.webprotege.permissions;


import edu.stanford.protege.webprotege.event.EventHandler;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 03/04/2013
 */
public interface PermissionsChangedHandler extends EventHandler {

    void handlePersmissionsChanged(PermissionsChangedEvent event);
}
