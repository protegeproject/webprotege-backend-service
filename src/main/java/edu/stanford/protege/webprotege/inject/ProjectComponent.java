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
 * 3 Oct 2016
 */
//@Subcomponent(
//        modules = {
//                ProjectModule.class,
//        }
//)
//@ProjectSingleton
public interface ProjectComponent {

    EagerProjectSingletons init();

    ProjectId getProjectId();

    EventManager<ProjectEvent<?>> getEventManager();

    ProjectDisposablesManager getDisposablesManager();

    ProjectActionHandlerRegistry getActionHandlerRegistry();

    RevisionManager getRevisionManager();

    EntityFrameFormDataComponent getEntityFrameFormDataComponentBuilder(EntityFrameFormDataModule module);

    FormDescriptorDtoTranslatorComponent getFormDescriptorDtoTranslatorComponent(EntityFrameFormDataModule module);
}

