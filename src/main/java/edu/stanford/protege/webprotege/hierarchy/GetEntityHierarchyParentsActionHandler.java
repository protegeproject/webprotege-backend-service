package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.entity.OWLEntityData;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.renderer.RenderingManager;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static edu.stanford.protege.webprotege.access.BuiltInAction.VIEW_PROJECT;

public class GetEntityHierarchyParentsActionHandler extends AbstractProjectActionHandler<GetHierarchyParentsAction, GetHierarchyParentsResult> {

    @Nonnull
    private final HierarchyProviderMapper hierarchyProviderMapper;

    @Nonnull
    private final RenderingManager renderingManager;

    @Inject
    public GetEntityHierarchyParentsActionHandler(@Nonnull AccessManager accessManager,
                                                  @Nonnull HierarchyProviderMapper hierarchyProviderMapper,
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
        HierarchyId hierarchyId = action.hierarchyId();
        Optional<HierarchyProvider<OWLEntity>> hierarchyProvider = hierarchyProviderMapper.getHierarchyProvider(hierarchyId);
        if (hierarchyProvider.isEmpty()) {
            return emptyResult();
        }
        OWLEntity entity = action.entity();
        List<OWLEntityData> parents = hierarchyProvider.get().getParents(entity)
                .stream()
                .filter(parent -> !parent.isTopEntity())
                .map(renderingManager::getRendering).collect(Collectors.toList());

        return new GetHierarchyParentsResult(entity, parents);
    }
}
