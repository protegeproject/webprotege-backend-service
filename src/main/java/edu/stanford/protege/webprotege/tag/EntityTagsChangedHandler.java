package edu.stanford.protege.webprotege.tag;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Mar 2018
 */
public interface EntityTagsChangedHandler {

    void handleEntityTagsChanged(@Nonnull EntityTagsChangedEvent event);
}
