package edu.stanford.protege.webprotege.app;

import edu.stanford.protege.webprotege.inject.ProjectComponent;
import edu.stanford.protege.webprotege.inject.project.ProjectModule;
import edu.stanford.protege.webprotege.project.ProjectComponentFactory;
import edu.stanford.protege.webprotege.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Jan 2018
 */
public class ProjectComponentFactoryImpl implements ProjectComponentFactory {

    @Nonnull
    private final ServerComponent serverComponent;

    @Inject
    public ProjectComponentFactoryImpl(@Nonnull ServerComponent serverComponent) {
        this.serverComponent = serverComponent;
    }

    @Nonnull
    @Override
    public ProjectComponent createProjectComponent(@Nonnull ProjectId projectId) {
        return serverComponent.getProjectComponent(new ProjectModule(projectId));
    }
}
