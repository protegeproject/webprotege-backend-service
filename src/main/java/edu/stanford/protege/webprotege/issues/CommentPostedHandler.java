package edu.stanford.protege.webprotege.issues;



import edu.stanford.protege.webprotege.event.EventHandler;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Oct 2016
 */
public interface CommentPostedHandler extends EventHandler {

    void handleCommentPosted(@Nonnull CommentPostedEvent commentPostedEvent);
}