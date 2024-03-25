package edu.stanford.protege.webprotege.dispatch.impl;

import edu.stanford.protege.webprotege.dispatch.ApplicationActionHandler;
import edu.stanford.protege.webprotege.inject.ApplicationSingleton;

import javax.inject.Inject;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Jun 2017
 */
@ApplicationSingleton
public class ApplicationActionHandlerRegistry extends ActionHandlerRegistryImpl {

    @Inject
    public ApplicationActionHandlerRegistry(Set<? extends ApplicationActionHandler> handlers) {
        super(handlers);
    }
}
