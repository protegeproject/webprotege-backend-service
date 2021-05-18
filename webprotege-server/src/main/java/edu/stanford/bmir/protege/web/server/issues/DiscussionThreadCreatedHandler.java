package edu.stanford.bmir.protege.web.server.issues;


import edu.stanford.bmir.protege.web.server.event.EventHandler;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Oct 2016
 */
public interface DiscussionThreadCreatedHandler extends EventHandler {

    void handleDiscussionThreadCreated(DiscussionThreadCreatedEvent event);
}
