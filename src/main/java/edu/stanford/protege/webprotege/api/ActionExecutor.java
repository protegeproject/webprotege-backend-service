package edu.stanford.protege.webprotege.api;

import edu.stanford.protege.webprotege.dispatch.DispatchServiceExecutor;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.dispatch.RequestContext;
import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.dispatch.ActionExecutionException;
import edu.stanford.protege.webprotege.dispatch.DispatchServiceResultContainer;
import edu.stanford.protege.webprotege.dispatch.Result;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Apr 2018
 *
 * An executor for actions that provides the necessary request and execution context.
 */
public class ActionExecutor {

    @Nonnull
    private final DispatchServiceExecutor executor;

    @Inject
    public ActionExecutor(@Nonnull DispatchServiceExecutor executor) {
        this.executor = checkNotNull(executor);
    }

    @SuppressWarnings("unchecked")
    public   <A extends Action<R>,  R extends Result> R execute(A action, ExecutionContext executionContext) {
        try {
            RequestContext requestContext = new RequestContext(executionContext.getUserId());
            DispatchServiceResultContainer resultContainer = executor.execute(action, requestContext, executionContext);
            return (R) resultContainer.getResult();
        } catch (ActionExecutionException e) {
            Throwable throwable = e.getCause();
            if(throwable instanceof RuntimeException) {
                throw ((RuntimeException) throwable);
            }
            else {
                throw new InternalServerErrorException();
            }
        }
    }
}
