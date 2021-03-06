package edu.stanford.protege.webprotege.change;

import edu.stanford.protege.webprotege.common.UserId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/02/2013
 */
public class ChangeGenerationContext {

    @Nonnull
    private final UserId userId;

    public ChangeGenerationContext(@Nonnull UserId userId) {
        this.userId = checkNotNull(userId);
    }

    @Nonnull
    public UserId getUserId() {
        return userId;
    }
}
