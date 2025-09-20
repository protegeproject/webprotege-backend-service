package edu.stanford.protege.webprotege.inject;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.impl.ProjectActionHandlerRegistry;
import edu.stanford.protege.webprotege.forms.*;
import edu.stanford.protege.webprotege.project.ProjectDisposablesManager;
import edu.stanford.protege.webprotege.revision.RevisionManager;
import org.springframework.context.ApplicationContext;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-13
 */
public class ProjectComponentImpl implements ProjectComponent {

    private final ApplicationContext applicationContext;

    private final ProjectId projectId;

    private final RevisionManager revisionManager;

    private final ProjectDisposablesManager projectDisposablesManager;

    private final ProjectActionHandlerRegistry projectActionHandlerRegistry;

    private final EntityFrameFormDataDtoBuilderFactory entityFrameFormDataDtoBuilderFactory;

    public ProjectComponentImpl(ApplicationContext projectContext,
                                ProjectId projectId,
                                RevisionManager revisionManager,
                                ProjectDisposablesManager projectDisposablesManager,
                                ProjectActionHandlerRegistry projectActionHandlerRegistry,
                                EntityFrameFormDataDtoBuilderFactory entityFrameFormDataDtoBuilderFactory) {
        this.applicationContext = projectContext;
        this.projectId = projectId;
        this.revisionManager = revisionManager;
        this.projectDisposablesManager = projectDisposablesManager;
        this.projectActionHandlerRegistry  = projectActionHandlerRegistry;
        this.entityFrameFormDataDtoBuilderFactory = entityFrameFormDataDtoBuilderFactory;
    }

    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Override
    public ProjectDisposablesManager getDisposablesManager() {
        return projectDisposablesManager;
    }

    @Override
    public ProjectActionHandlerRegistry getActionHandlerRegistry() {
        return projectActionHandlerRegistry;
    }

    @Override
    public RevisionManager getRevisionManager() {
        return revisionManager;
    }

    @Override
    public EntityFrameFormDataDtoBuilder getEntityFrameFormDataComponentBuilder(EntityFormDataRequestSpec requestSpec) {
        return entityFrameFormDataDtoBuilderFactory.getFormDataDtoBuilder(
                applicationContext,
                requestSpec
        );
    }

    @Override
    public FormDescriptorDtoTranslatorComponent getFormDescriptorDtoTranslatorComponent(EntityFormDataRequestSpec module) {
        return null;
    }
}
