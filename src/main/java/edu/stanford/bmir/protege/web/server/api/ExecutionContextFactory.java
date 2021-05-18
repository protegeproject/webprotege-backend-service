package edu.stanford.bmir.protege.web.server.api;

import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import org.glassfish.hk2.api.Factory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.server.api.AuthenticationConstants.EXECUTION_CONTEXT;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-15
 */
public class ExecutionContextFactory implements Factory<ExecutionContext> {

    @Context
    private HttpServletRequest request;

    @Inject
    public ExecutionContextFactory(@Nonnull HttpServletRequest request) {
        this.request = checkNotNull(request);
    }


    @Override
    public ExecutionContext provide() {
        // Provide the ExecutionContext from the EXECUTION_CONTEXT attribute that should have
        // been set by the AuthenticationFilter.
        ExecutionContext executionContext = (ExecutionContext) request.getAttribute(EXECUTION_CONTEXT);
        if(executionContext == null) {
            throw new IllegalStateException(EXECUTION_CONTEXT + " attribute is not set");
        }
        return executionContext;
    }

    @Override
    public void dispose(ExecutionContext instance) {

    }
}
