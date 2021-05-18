package edu.stanford.bmir.protege.web.server.issues;



import edu.stanford.bmir.protege.web.server.event.EventHandler;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Oct 2016
 */
public interface CommentPostedHandler extends EventHandler {

    void handleCommentPosted(@Nonnull CommentPostedEvent commentPostedEvent);
}
