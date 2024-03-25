package edu.stanford.protege.webprotege.forms;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;


import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-28
 */
public class GetEntityCreationFormsActionHandler extends AbstractProjectActionHandler<GetEntityCreationFormsAction, GetEntityCreationFormsResult> {

    @Nonnull
    private final EntityFormManager entityFormManager;


    @Inject
    public GetEntityCreationFormsActionHandler(@Nonnull AccessManager accessManager,
                                               @Nonnull EntityFormManager entityFormManager) {
        super(accessManager);
        this.entityFormManager = checkNotNull(entityFormManager);
    }

    @Nonnull
    @Override
    public Class<GetEntityCreationFormsAction> getActionClass() {
        return GetEntityCreationFormsAction.class;
    }

    @Nonnull
    @Override
    public GetEntityCreationFormsResult execute(@Nonnull GetEntityCreationFormsAction action,
                                                @Nonnull ExecutionContext executionContext) {
        return new GetEntityCreationFormsResult(ImmutableList.of());
//        var entityCreationForms = entityFormManager.getFormDescriptors(action.getParentEntity(),
//                                             action.getProjectId(),
//                                             FormPurpose.ENTITY_CREATION);
//
//        var formDtoTranslatorComponent = projectComponent.getFormDescriptorDtoTranslatorComponent(new EntityFrameFormDataModule());
//        var formDtoTranslator = formDtoTranslatorComponent.getFormDescriptorDtoTranslator();
//        var formDtos = entityCreationForms.stream()
//                           .map(formDtoTranslator::toFormDescriptorDto)
//                           .collect(toImmutableList());
//        return GetEntityCreationFormsResult.get(formDtos);
    }
}
