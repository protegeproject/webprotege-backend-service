package edu.stanford.protege.webprotege.issues;


import edu.stanford.protege.webprotege.event.EventHandler;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Oct 2016
 */
public interface DiscussionThreadStatusChangedHandler extends EventHandler {

    void handleDiscussionThreadStatusChanged(DiscussionThreadStatusChangedEvent event);
}
