package edu.stanford.protege.webprotege.dispatch;

import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/01/2013
 */
public class RequestContext {

    private final UserId userId;

    private final ExecutionContext executionContext;

    public RequestContext(UserId userId, ExecutionContext executionContext) {
        this.userId = checkNotNull(userId, "userId must not be null");
        this.executionContext = executionContext;
    }

    /**
     * Gets the {@link UserId} for this request.
     *
     * @return The {@link UserId}.  Not {@code null}.
     */
    public UserId getUserId() {
        return userId;
    }

    public ExecutionContext getExecutionContext() {
        return executionContext;
    }

    @Override
    public int hashCode() {
        return "RequestContext".hashCode() + userId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof RequestContext)) {
            return false;
        }
        RequestContext other = (RequestContext) obj;
        return this.userId.equals(other.userId);
    }


}
