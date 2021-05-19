package edu.stanford.protege.webprotege.event;



/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-11-04
 */
public interface LargeNumberOfChangesHandler extends EventHandler {

    void handleLargeNumberOfChanges(LargeNumberOfChangesEvent event);
}
