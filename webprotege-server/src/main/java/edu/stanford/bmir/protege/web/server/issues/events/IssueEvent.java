package edu.stanford.bmir.protege.web.server.issues.events;


import edu.stanford.bmir.protege.web.server.user.UserId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26 Sep 16
 */
public interface IssueEvent {

    @Nonnull
    UserId getUserId();

    long getTimestamp();
}
