package edu.stanford.protege.webprotege.entity;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInCapability;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.index.DeprecatedEntitiesIndex;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.renderer.RenderingManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import jakarta.inject.Inject;

import static edu.stanford.protege.webprotege.access.BuiltInCapability.VIEW_PROJECT;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Jun 2017
 */
public class GetDeprecatedEntitiesActionHandler extends AbstractProjectActionHandler<GetDeprecatedEntitiesAction, GetDeprecatedEntitiesResult> {

    @Nonnull
    private final DeprecatedEntitiesIndex deprecatedEntitiesIndex;

    @Nonnull
    private final RenderingManager renderingManager;

    @Inject
    public GetDeprecatedEntitiesActionHandler(@Nonnull AccessManager accessManager,
                                              @Nonnull RenderingManager renderingManager,
                                              @Nonnull DeprecatedEntitiesIndex deprecatedEntitiesIndex) {
        super(accessManager);
        this.renderingManager = renderingManager;
        this.deprecatedEntitiesIndex = deprecatedEntitiesIndex;
    }

    @Nonnull
    @Override
    public Class<GetDeprecatedEntitiesAction> getActionClass() {
        return GetDeprecatedEntitiesAction.class;
    }

    @Nullable
    @Override
    protected BuiltInCapability getRequiredExecutableBuiltInAction(GetDeprecatedEntitiesAction action) {
        return VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public GetDeprecatedEntitiesResult execute(@Nonnull GetDeprecatedEntitiesAction action,
                                               @Nonnull ExecutionContext executionContext) {
        var pageRequest = action.pageRequest();
        var entityTypes = action.entityTypes();
        var deprecatedEntitiesPage = deprecatedEntitiesIndex.getDeprecatedEntities(entityTypes,
                                                                                   pageRequest);
        var entityDataPage = deprecatedEntitiesPage.transform(renderingManager::getRendering);
        return new GetDeprecatedEntitiesResult(entityDataPage);
    }


}
