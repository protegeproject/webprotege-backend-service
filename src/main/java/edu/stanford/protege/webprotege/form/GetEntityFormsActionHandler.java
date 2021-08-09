package edu.stanford.protege.webprotege.form;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.renderer.RenderingManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.function.Predicate;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-01
 */
public class GetEntityFormsActionHandler extends AbstractProjectActionHandler<GetEntityFormsAction, GetEntityFormsResult> {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final EntityFormManager formManager;

    @Nonnull
    private final RenderingManager renderingManager;

    @Inject
    public GetEntityFormsActionHandler(@Nonnull AccessManager accessManager,
                                       @Nonnull ProjectId projectId,
                                       @Nonnull EntityFormManager formManager,
                                       @Nonnull RenderingManager renderingManager) {
        super(accessManager);
        this.projectId = projectId;
        this.formManager = formManager;
        this.renderingManager = renderingManager;
    }

    @Nonnull
    @Override
    public GetEntityFormsResult execute(@Nonnull GetEntityFormsAction action,
                                        @Nonnull ExecutionContext executionContext) {
        var pageRequests = action.getFormPageRequests();
        var pageRequestIndex = FormPageRequestIndex.create(ImmutableList.copyOf(pageRequests));
        var entity = action.getEntity();
        var langTagFilter = action.getLangTagFilter();
        var ordering = action.getGridControlOrdering();
        var formRegionOrderingIndex = FormRegionOrderingIndex.get(ordering);
        var formRegionFilterIndex = FormRegionFilterIndex.get(action.getFormRegionFilters());
        var module = new EntityFrameFormDataModule(formRegionOrderingIndex,
                                                   langTagFilter,
                                                   pageRequestIndex,
                                                   formRegionFilterIndex);
        throw new RuntimeException("Needs fixing");
//        var formDataDtoBuilder = projectComponent.getEntityFrameFormDataComponentBuilder(module).formDataBuilder();
//        var formsFilterList = action.getFormFilters();
//        var formSubject = Optional.of(FormEntitySubject.get(entity));
//        var forms = formManager.getFormDescriptors(entity, projectId, FormPurpose.ENTITY_EDITING)
//                               .stream()
//                               .filter(byFormIds(formsFilterList))
//                               .map(formDescriptor -> formDataDtoBuilder.toFormData(formSubject, formDescriptor))
//                               .collect(toImmutableList());
//
//        var entityData = renderingManager.getRendering(entity);
//        return GetEntityFormsResult.create(entityData, ImmutableList.copyOf(action.getFormFilters()), forms);
    }

    public static Predicate<FormDescriptor> byFormIds(ImmutableSet<FormId> formsFilterList) {
        return (FormDescriptor formDescriptor) -> formsFilterList.isEmpty() || formsFilterList.contains(formDescriptor.getFormId());
    }

    @Nonnull
    @Override
    public Class<GetEntityFormsAction> getActionClass() {
        return GetEntityFormsAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetEntityFormsAction action) {
        return BuiltInAction.VIEW_PROJECT;
    }
}
