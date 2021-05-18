package edu.stanford.bmir.protege.web.server.event;



/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/03/2013
 */
public interface ProjectChangedHandler extends EventHandler {

    void handleProjectChanged(ProjectChangedEvent event);
}
