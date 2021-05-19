package edu.stanford.protege.webprotege.hierarchy;


import edu.stanford.protege.webprotege.event.EventHandler;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 1 Dec 2017
 */
public interface EntityHierarchyChangedHandler extends EventHandler {
    void handleHierarchyChanged(EntityHierarchyChangedEvent event);
}
