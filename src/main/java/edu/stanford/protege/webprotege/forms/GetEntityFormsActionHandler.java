package edu.stanford.protege.webprotege.forms;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.forms.data.FormEntitySubject;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.renderer.RenderingManager;
import org.springframework.context.ApplicationContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Optional;
import java.util.function.Predicate;

import static com.google.common.collect.ImmutableList.toImmutableList;

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

    @Nonnull
    private final ApplicationContext context;

    private final EntityFrameFormDataDtoBuilderFactory entityFrameFormDataDtoBuilderFactory;

    @Inject
    public GetEntityFormsActionHandler(@Nonnull AccessManager accessManager,
                                       @Nonnull ProjectId projectId,
                                       @Nonnull EntityFormManager formManager,
                                       @Nonnull RenderingManager renderingManager,
                                       @Nonnull ApplicationContext context,
                                       EntityFrameFormDataDtoBuilderFactory entityFrameFormDataDtoBuilderFactory) {
        super(accessManager);
        this.projectId = projectId;
        this.formManager = formManager;
        this.renderingManager = renderingManager;
        this.context = context;
        this.entityFrameFormDataDtoBuilderFactory = entityFrameFormDataDtoBuilderFactory;
    }

    @Nonnull
    @Override
    public GetEntityFormsResult execute(@Nonnull GetEntityFormsAction action,
                                        @Nonnull ExecutionContext executionContext) {
        var pageRequests = action.formPageRequests();
        var pageRequestIndex = FormPageRequestIndex.create(ImmutableList.copyOf(pageRequests));
        var entity = action.entity();
        var langTagFilter = action.langTagFilter();
        var ordering = action.formRegionOrderings();
        var formRegionOrderingIndex = FormRegionOrderingIndex.get(ordering);
        var formRegionFilterIndex = FormRegionFilterIndex.get(action.formRegionFilters());

        var formDataDtoBuilder = entityFrameFormDataDtoBuilderFactory.getFormDataDtoBuilder(
                context,
                new EntityFormDataRequestSpec(formRegionOrderingIndex,
                                              langTagFilter,
                                              pageRequestIndex,
                                              formRegionFilterIndex)
        );


        var formsFilterList = action.formFilters();
        var formSubject = Optional.of(FormEntitySubject.get(entity));
        var forms = formManager.getFormDescriptors(entity, projectId, FormPurpose.ENTITY_EDITING)
                               .stream()
                               .filter(byFormIds(formsFilterList))
                               .map(formDescriptor -> formDataDtoBuilder.toFormData(formSubject, formDescriptor))
                               .collect(toImmutableList());
        var entityData = renderingManager.getRendering(entity);
        return new GetEntityFormsResult(entityData, ImmutableList.copyOf(action.formFilters()), forms);
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
