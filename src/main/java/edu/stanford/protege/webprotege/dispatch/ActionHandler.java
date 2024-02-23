package edu.stanford.protege.webprotege.dispatch;

import edu.stanford.protege.webprotege.common.Request;
import edu.stanford.protege.webprotege.common.Response;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/01/2013
 * <p>
 *     Handles {@link Action}s from a client request.  An {@link ActionHandler} supplies a {@link RequestValidator}
 *     which can be used to determine whether an action can be executed within a given request context.
 * </p>
 * <p>
 *     Action handlers are <b>stateless</b>.  They are thread safe.
 * </p>
 */
@ThreadSafe
public interface ActionHandler<A extends Request<R>, R extends Response> {

    /**
     * Gets the class of {@link Action} handled by this handler.
     * @return The class of {@link Action}.  Not {@code null}.
     */
    @Nonnull
    Class<A> getActionClass();

    @Nonnull
    RequestValidator getRequestValidator(@Nonnull A action, @Nonnull RequestContext requestContext);

    @Nonnull
    R execute(@Nonnull A action, @Nonnull ExecutionContext executionContext);

}
