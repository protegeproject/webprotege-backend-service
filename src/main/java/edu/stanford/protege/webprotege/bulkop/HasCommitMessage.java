package edu.stanford.protege.webprotege.bulkop;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 1 Oct 2018
 */
public interface HasCommitMessage {

    @Nonnull
    String getCommitMessage();
}
