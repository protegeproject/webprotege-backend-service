package edu.stanford.protege.webprotege.inject;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.impl.ProjectActionHandlerRegistry;
import edu.stanford.protege.webprotege.forms.*;
import edu.stanford.protege.webprotege.project.ProjectDisposablesManager;
import edu.stanford.protege.webprotege.project.ProjectHistoryReplacer;
import edu.stanford.protege.webprotege.revision.RevisionManager;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 3 Oct 2016
 */
public interface ProjectComponent {

    ProjectId getProjectId();

    ProjectDisposablesManager getDisposablesManager();

    ProjectActionHandlerRegistry getActionHandlerRegistry();

    RevisionManager getRevisionManager();

    EntityFrameFormDataDtoBuilder getEntityFrameFormDataComponentBuilder(EntityFormDataRequestSpec formDataConfiguration);

    FormDescriptorDtoTranslatorComponent getFormDescriptorDtoTranslatorComponent(EntityFormDataRequestSpec module);
}

