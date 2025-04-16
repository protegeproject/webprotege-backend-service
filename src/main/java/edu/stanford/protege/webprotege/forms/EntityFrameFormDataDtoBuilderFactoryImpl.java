package edu.stanford.protege.webprotege.forms;

import edu.stanford.protege.webprotege.*;
import edu.stanford.protege.webprotege.authorization.Capability;
import edu.stanford.protege.webprotege.authorization.GetAuthorizedCapabilitiesRequest;
import edu.stanford.protege.webprotege.authorization.GetAuthorizedCapabilitiesResponse;
import edu.stanford.protege.webprotege.common.LangTagFilter;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.forms.EntityFormDataRequestSpec.FormRegionAccessRestrictionsList;
import edu.stanford.protege.webprotege.forms.EntityFormDataRequestSpec.UserCapabilities;
import edu.stanford.protege.webprotege.ipc.CommandExecutor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-04-09
 */
public class EntityFrameFormDataDtoBuilderFactoryImpl implements EntityFrameFormDataDtoBuilderFactory {

    public EntityFrameFormDataDtoBuilderFactoryImpl() {
    }

    @Override
    public EntityFrameFormDataDtoBuilder getFormDataDtoBuilder(ApplicationContext projectContext,
                                                               EntityFormDataRequestSpec requestSpec) {
        var formsContext = new AnnotationConfigApplicationContext();
        formsContext.setParent(projectContext);
        var projectId = projectContext.getBean("projectId", ProjectId.class);
        formsContext.setDisplayName("Project-Forms-Context for " + projectId);
        formsContext.register(FormBeansConfiguration.class);
        formsContext.register(FormDataBuilderConfiguration.class);
        formsContext.register(EntityMatcherBeansConfiguration.class);
        formsContext.registerBean("formRegionOrderingIndex", FormRegionOrderingIndex.class,
                                  requestSpec::formRegionOrderingIndex);
        formsContext.registerBean("langTagFilter", LangTagFilter.class, requestSpec::langTagFilter);
        formsContext.registerBean("formPageRequestIndex", FormPageRequestIndex.class, requestSpec::pageRequestIndex);
        formsContext.registerBean("formRegionFilterIndex", FormRegionFilterIndex.class, requestSpec::formRegionFilterIndex);
        formsContext.registerBean("userCapabilities", UserCapabilities.class , requestSpec::userCapabilities);
        formsContext.registerBean("formRegionAccessRestrictions", FormRegionAccessRestrictionsList.class, requestSpec::formRegionAccessRestrictions);
        formsContext.refresh();
        return formsContext.getBean(EntityFrameFormDataDtoBuilder.class);
    }
}
