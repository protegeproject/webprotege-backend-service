package edu.stanford.protege.webprotege.forms;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInCapability;
import edu.stanford.protege.webprotege.authorization.*;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.forms.EntityFormDataRequestSpec.FormRegionAccessRestrictionsList;
import edu.stanford.protege.webprotege.forms.EntityFormDataRequestSpec.UserCapabilities;
import edu.stanford.protege.webprotege.forms.data.FormEntitySubject;
import edu.stanford.protege.webprotege.ipc.CommandExecutor;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.renderer.RenderingManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;

import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-01
 */
public class GetEntityFormsActionHandler extends AbstractProjectActionHandler<GetEntityFormsAction, GetEntityFormsResult> {

    private Logger logger = LoggerFactory.getLogger(GetEntityFormsActionHandler.class);

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final EntityFormManager formManager;

    @Nonnull
    private final RenderingManager renderingManager;

    @Nonnull
    private final ApplicationContext context;

    private final EntityFrameFormDataDtoBuilderFactory entityFrameFormDataDtoBuilderFactory;

    private final CommandExecutor<GetAuthorizedCapabilitiesRequest, GetAuthorizedCapabilitiesResponse> getCapsExecutor;

    private final CommandExecutor<GetFormRegionAccessRestrictionsRequest, GetFormRegionAccessRestrictionsResponse> getFormRegionAccessRestrictionsExecutor;

    @Inject
    public GetEntityFormsActionHandler(@Nonnull AccessManager accessManager,
                                       @Nonnull ProjectId projectId,
                                       @Nonnull EntityFormManager formManager,
                                       @Nonnull RenderingManager renderingManager,
                                       @Nonnull ApplicationContext context,
                                       EntityFrameFormDataDtoBuilderFactory entityFrameFormDataDtoBuilderFactory, AccessManager accessManager1, CommandExecutor<GetAuthorizedCapabilitiesRequest, GetAuthorizedCapabilitiesResponse> getCapsExecutor, CommandExecutor<GetFormRegionAccessRestrictionsRequest, GetFormRegionAccessRestrictionsResponse> getFormRegionAccessRestrictionsExecutor) {
        super(accessManager);
        this.projectId = projectId;
        this.formManager = formManager;
        this.renderingManager = renderingManager;
        this.context = context;
        this.entityFrameFormDataDtoBuilderFactory = entityFrameFormDataDtoBuilderFactory;
        this.getCapsExecutor = getCapsExecutor;
        this.getFormRegionAccessRestrictionsExecutor = getFormRegionAccessRestrictionsExecutor;
    }

    @Nonnull
    @Override
    public GetEntityFormsResult execute(@Nonnull GetEntityFormsAction action,
                                        @Nonnull ExecutionContext executionContext) {
        var capabilities = getCapabilities(action.projectId(), executionContext.userId(), executionContext);
        var accessRestrictions = getFormRegionAccessRestrictions(projectId, executionContext);
        logger.info("GetEntityFormsActionHandler got capabilities: {}", capabilities);
        var entityData = renderingManager.getRendering(action.entity());
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
                                                  formRegionFilterIndex,
                            new UserCapabilities(capabilities),
                            new FormRegionAccessRestrictionsList(accessRestrictions))
            );

            var formsFilterList = action.formFilters();
            var formSubject = Optional.of(FormEntitySubject.get(entity));
            var forms = formManager.getFormDescriptors(entity, projectId, FormPurpose.ENTITY_EDITING)
                                   .stream()
                                   .filter(byFormIds(formsFilterList))
                                   .map(formDescriptor -> formDataDtoBuilder.toFormData(formSubject, formDescriptor))
                                   .collect(toImmutableList());


            return new GetEntityFormsResult(entityData, ImmutableList.copyOf(action.formFilters()), forms);
    }

    private Set<Capability> getCapabilities(@NotNull GetEntityFormsAction action, @NotNull ExecutionContext executionContext) throws InterruptedException, ExecutionException {
        var capsResponse = getCapsExecutor.execute(new GetAuthorizedCapabilitiesRequest(ProjectResource.forProject(action.projectId()),
                Subject.forUser(executionContext.userId())), executionContext)
                .get();
        return capsResponse.capabilities();
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
    protected BuiltInCapability getRequiredExecutableBuiltInAction(GetEntityFormsAction action) {
        return BuiltInCapability.VIEW_PROJECT;
    }

    private Set<Capability> getCapabilities(ProjectId projectId, UserId userId, ExecutionContext executionContext) {
        try {
            var result = getCapsExecutor.execute(new GetAuthorizedCapabilitiesRequest(ProjectResource.forProject(projectId),
                    Subject.forUser(userId)), executionContext)
                    .get();
            return result.capabilities();
        } catch (InterruptedException | ExecutionException e) {
            logger.error("An error occurred while retrieving the capabilites for the specified user and project", e);
            return Set.of();
        }
    }

    private List<FormRegionAccessRestrictions> getFormRegionAccessRestrictions(ProjectId projectId, ExecutionContext executionContext) throws RuntimeException {
        try {
            return getFormRegionAccessRestrictionsExecutor.execute(new GetFormRegionAccessRestrictionsRequest(projectId), executionContext)
                    .get()
                    .accessRestrictions();
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Could not retrieve form access restrictions");
            return List.of();
        }
    }
}
