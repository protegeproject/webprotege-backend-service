package edu.stanford.protege.webprotege.dispatch;

import edu.stanford.protege.webprotege.common.UserId;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/01/2013
 * <p>
 *     Describes the context in which an action is being executed.
 * </p>
 */
public class ExecutionContext {

    private final UserId userId;

    public ExecutionContext(UserId userId) {
        this.userId = userId;
    }

    public UserId getUserId() {
        return userId;
    }

}
