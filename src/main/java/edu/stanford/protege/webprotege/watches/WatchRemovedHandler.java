package edu.stanford.protege.webprotege.watches;


import edu.stanford.protege.webprotege.event.EventHandler;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/03/2013
 */
public interface WatchRemovedHandler extends EventHandler {

    void handleWatchRemoved(WatchRemovedEvent event);
}
