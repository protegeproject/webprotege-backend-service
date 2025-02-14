package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.access.*;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.renderer.RenderingManager;

import javax.annotation.*;
import javax.inject.Inject;
import java.util.Collections;
import java.util.stream.Collectors;

import static edu.stanford.protege.webprotege.access.BuiltInAction.VIEW_PROJECT;

public class GetEntityHierarchyParentsActionHandler extends AbstractProjectActionHandler<GetHierarchyParentsAction, GetHierarchyParentsResult> {

    @Nonnull
    private final HierarchyProviderManager hierarchyProviderMapper;

    @Nonnull
    private final RenderingManager renderingManager;

    @Inject
    public GetEntityHierarchyParentsActionHandler(@Nonnull AccessManager accessManager,
                                                  @Nonnull HierarchyProviderManager hierarchyProviderMapper,
                                                  @Nonnull RenderingManager renderingManager) {
        super(accessManager);
        this.hierarchyProviderMapper = hierarchyProviderMapper;
        this.renderingManager = renderingManager;
    }

    static GetHierarchyParentsResult emptyResult() {
        return new GetHierarchyParentsResult(null, Collections.EMPTY_LIST);
    }

    @Nonnull
    @Override
    public Class<GetHierarchyParentsAction> getActionClass() {
        return GetHierarchyParentsAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetHierarchyParentsAction action) {
        return VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public GetHierarchyParentsResult execute(@Nonnull GetHierarchyParentsAction action, @Nonnull ExecutionContext executionContext) {
        var hierarchyDescriptor = action.hierarchyDescriptor();
        var hierarchyProvider = hierarchyProviderMapper.getHierarchyProvider(hierarchyDescriptor);
        if (hierarchyProvider.isEmpty()) {
            return emptyResult();
        }
        var entity = action.entity();
        var parents = hierarchyProvider.get().getParents(entity)
                .stream()
                .filter(parent -> !parent.isTopEntity())
                .map(renderingManager::getRendering)
                .collect(Collectors.toList());

        return new GetHierarchyParentsResult(entity, parents);
    }
}
