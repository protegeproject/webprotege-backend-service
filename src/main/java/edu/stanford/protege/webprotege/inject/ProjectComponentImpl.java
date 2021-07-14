package edu.stanford.protege.webprotege.inject;

import edu.stanford.protege.webprotege.dispatch.impl.ProjectActionHandlerRegistry;
import edu.stanford.protege.webprotege.event.ProjectEvent;
import edu.stanford.protege.webprotege.events.EventManager;
import edu.stanford.protege.webprotege.form.EntityFrameFormDataComponent;
import edu.stanford.protege.webprotege.form.EntityFrameFormDataModule;
import edu.stanford.protege.webprotege.form.FormDescriptorDtoTranslatorComponent;
import edu.stanford.protege.webprotege.project.ProjectDisposablesManager;
import edu.stanford.protege.webprotege.project.ProjectId;
import edu.stanford.protege.webprotege.revision.RevisionManager;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-13
 */
public class ProjectComponentImpl implements ProjectComponent {

    private final ProjectId projectId;

    private final EventManager<ProjectEvent<?>> eventManager;

    private final RevisionManager revisionManager;

    private final ProjectDisposablesManager projectDisposablesManager;

    public ProjectComponentImpl(ProjectId projectId,
                                EventManager<ProjectEvent<?>> eventManager,
                                RevisionManager revisionManager, ProjectDisposablesManager projectDisposablesManager) {
        this.projectId = projectId;
        this.eventManager = eventManager;
        this.revisionManager = revisionManager;
        this.projectDisposablesManager = projectDisposablesManager;
    }

    @Override
    public EagerProjectSingletons init() {
        return null;
    }

    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Override
    public EventManager<ProjectEvent<?>> getEventManager() {
        return eventManager;
    }

    @Override
    public ProjectDisposablesManager getDisposablesManager() {
        return projectDisposablesManager;
    }

    @Override
    public ProjectActionHandlerRegistry getActionHandlerRegistry() {
        return null;
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
