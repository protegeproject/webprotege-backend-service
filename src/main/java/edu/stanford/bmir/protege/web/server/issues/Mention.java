package edu.stanford.bmir.protege.web.server.issues;


import edu.stanford.bmir.protege.web.server.user.UserId;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26 Sep 16
 *
 * Represents the mention of some object in String
 */
public abstract class Mention {

    /**
     * If this mention mentions a UserId then this method returns the UserId.
     * @return The UserId.
     */
    @Nonnull
    public Optional<UserId> getMentionedUserId() {
        return Optional.empty();
    }
}
