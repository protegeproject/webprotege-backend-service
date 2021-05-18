package edu.stanford.bmir.protege.web.server.permissions;


import edu.stanford.bmir.protege.web.server.event.EventHandler;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 03/04/2013
 */
public interface PermissionsChangedHandler extends EventHandler {

    void handlePersmissionsChanged(PermissionsChangedEvent event);
}
