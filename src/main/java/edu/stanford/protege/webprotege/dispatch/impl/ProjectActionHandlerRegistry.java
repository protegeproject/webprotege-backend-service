package edu.stanford.protege.webprotege.dispatch.impl;

import edu.stanford.protege.webprotege.dispatch.ProjectActionHandler;
import edu.stanford.protege.webprotege.inject.ProjectSingleton;

import javax.inject.Inject;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Jun 2017
 */
@ProjectSingleton
public class ProjectActionHandlerRegistry extends ActionHandlerRegistryImpl {

    @Inject
    public ProjectActionHandlerRegistry(Set<ProjectActionHandler<?, ?>> handlers) {
        super(handlers);
    }
}
