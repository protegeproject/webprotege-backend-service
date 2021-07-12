package edu.stanford.protege.webprotege.app;

import edu.stanford.protege.webprotege.inject.ProjectComponent;
import edu.stanford.protege.webprotege.inject.project.ProjectModule;
import edu.stanford.protege.webprotege.project.ProjectComponentFactory;
import edu.stanford.protege.webprotege.project.ProjectId;
import org.springframework.context.ApplicationContext;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Jan 2018
 */
public class ProjectComponentFactoryImpl implements ProjectComponentFactory {

    private final ApplicationContext applicationContext;

    @Inject
    public ProjectComponentFactoryImpl(@Nonnull ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Nonnull
    @Override
    public ProjectComponent createProjectComponent(@Nonnull ProjectId projectId) {
        return null;
    }
}
