package edu.stanford.protege.webprotege.api;

import edu.stanford.protege.webprotege.common.Request;
import edu.stanford.protege.webprotege.common.Response;
import edu.stanford.protege.webprotege.dispatch.*;
import edu.stanford.protege.webprotege.ipc.CommandExecutionException;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.permissions.PermissionDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Apr 2018
 *
 * An executor for actions that provides the necessary request and execution context.
 */
public class ActionExecutor {

    private static final Logger logger = LoggerFactory.getLogger(ActionExecutor.class);

    @Nonnull
    private final DispatchServiceExecutor executor;

    @Inject
    public ActionExecutor(@Nonnull DispatchServiceExecutor executor) {
        this.executor = checkNotNull(executor);
    }

    @SuppressWarnings("unchecked")
    public   <A extends Request<R>,  R extends Response> R execute(A action, ExecutionContext executionContext) {
        try {
            RequestContext requestContext = new RequestContext(executionContext.userId(), new edu.stanford.protege.webprotege.dispatch.ExecutionContext(executionContext.userId(), executionContext.jwt()));
            DispatchServiceResultContainer resultContainer = executor.execute(action, requestContext, new edu.stanford.protege.webprotege.dispatch.ExecutionContext(executionContext.userId(), executionContext.jwt()));
            return (R) resultContainer.getResult();
        } catch (ActionExecutionException e) {
            logger.info("Action execution exception while executing request: {}", e.getMessage(), e);
            throw new CommandExecutionException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @SuppressWarnings("unchecked")
    public <A extends Request<R>,  R extends Response> Mono<R> executeRequest(A request, ExecutionContext executionContext) {
        try {
            var requestContext = new RequestContext(executionContext.userId(), new edu.stanford.protege.webprotege.dispatch.ExecutionContext(executionContext.userId(), executionContext.jwt()));
            var resultContainer = executor.execute(request, requestContext, new edu.stanford.protege.webprotege.dispatch.ExecutionContext(executionContext.userId(), executionContext.jwt()));
            var result = (R) resultContainer.getResult();
            return Mono.just(result);
        } catch (PermissionDeniedException e) {
            logger.info("PermissionDeniedException thrown while handling request", e);
            return Mono.error(new CommandExecutionException(HttpStatus.FORBIDDEN));
        } catch (Exception e) {
            logger.info("Exception thrown while handling request", e);
            return Mono.error(new CommandExecutionException(HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

}
