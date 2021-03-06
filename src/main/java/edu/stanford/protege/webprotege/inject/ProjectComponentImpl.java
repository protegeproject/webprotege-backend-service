package edu.stanford.protege.webprotege.inject;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.impl.ProjectActionHandlerRegistry;
import edu.stanford.protege.webprotege.forms.EntityFrameFormDataComponent;
import edu.stanford.protege.webprotege.forms.EntityFrameFormDataModule;
import edu.stanford.protege.webprotege.forms.FormDescriptorDtoTranslatorComponent;
import edu.stanford.protege.webprotege.project.ProjectDisposablesManager;
import edu.stanford.protege.webprotege.revision.RevisionManager;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-13
 */
public class ProjectComponentImpl implements ProjectComponent {

    private final ProjectId projectId;

    private final RevisionManager revisionManager;

    private final ProjectDisposablesManager projectDisposablesManager;

    private final ProjectActionHandlerRegistry projectActionHandlerRegistry;

    public ProjectComponentImpl(ProjectId projectId,
                                RevisionManager revisionManager,
                                ProjectDisposablesManager projectDisposablesManager,
                                ProjectActionHandlerRegistry projectActionHandlerRegistry) {
        this.projectId = projectId;
        this.revisionManager = revisionManager;
        this.projectDisposablesManager = projectDisposablesManager;
        this.projectActionHandlerRegistry  = projectActionHandlerRegistry;
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
    public EntityFrameFormDataComponent getEntityFrameFormDataComponentBuilder(EntityFrameFormDataModule module) {
        return null;
    }

    @Override
    public FormDescriptorDtoTranslatorComponent getFormDescriptorDtoTranslatorComponent(EntityFrameFormDataModule module) {
        return null;
    }
}
