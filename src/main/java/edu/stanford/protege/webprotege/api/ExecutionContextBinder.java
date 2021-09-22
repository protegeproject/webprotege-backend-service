package edu.stanford.protege.webprotege.api;

import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-15
 */
@Deprecated
public class ExecutionContextBinder extends AbstractBinder {

    @Override
    protected void configure() {
        bindFactory(ExecutionContextFactory.class).to(ExecutionContext.class)
                                        .proxy(false)
                                        .proxyForSameScope(false)
                                        .in(RequestScoped.class);
    }
}
