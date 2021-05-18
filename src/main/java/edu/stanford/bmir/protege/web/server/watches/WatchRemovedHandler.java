package edu.stanford.bmir.protege.web.server.watches;


import edu.stanford.bmir.protege.web.server.event.EventHandler;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/03/2013
 */
public interface WatchRemovedHandler extends EventHandler {

    void handleWatchRemoved(WatchRemovedEvent event);
}
