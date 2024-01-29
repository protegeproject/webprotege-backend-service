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
    private final String jwt;

    public ExecutionContext(UserId userId) {
        this.userId = checkNotNull(userId);
        this.jwt = "";
    }
    public ExecutionContext(UserId userId, String jwt) {
        this.userId = userId;
        this.jwt = jwt;
    }

    public UserId getUserId() {
        return userId;
    }

    public String getJwt() {
        return jwt;
    }
}
