package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.entity.EntityNode;
import edu.stanford.protege.webprotege.entity.EntityNodeRenderer;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.protege.webprotege.access.BuiltInAction.VIEW_PROJECT;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 30 Nov 2017
 */
public class GetHierarchyRootsActionHandler extends AbstractProjectActionHandler<GetHierarchyRootsAction, GetHierarchyRootsResult> {

    @Nonnull
    private final HierarchyProviderMapper hierarchyProviderMapper;

    @Nonnull
    private final EntityNodeRenderer renderer;

    @Inject
    public GetHierarchyRootsActionHandler(@Nonnull AccessManager accessManager,
                                          @Nonnull HierarchyProviderMapper hierarchyProviderMapper,
                                          @Nonnull EntityNodeRenderer renderer) {
        super(accessManager);
        this.hierarchyProviderMapper = checkNotNull(hierarchyProviderMapper);
        this.renderer = checkNotNull(renderer);
    }

    @Nonnull
    @Override
    public Class<GetHierarchyRootsAction> getActionClass() {
        return GetHierarchyRootsAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetHierarchyRootsAction action) {
        return VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public GetHierarchyRootsResult execute(@Nonnull GetHierarchyRootsAction action, @Nonnull ExecutionContext executionContext) {
        HierarchyId hierarchyId = action.hierarchyId();
        return hierarchyProviderMapper.getHierarchyProvider(hierarchyId).map(hierarchyProvider -> {
            Collection<OWLEntity> roots = hierarchyProvider.getRoots();
            List<GraphNode<EntityNode>> rootNodes =
                    roots.stream()
                         .map(rootEntity -> {
                             EntityNode rootNode = renderer.render(rootEntity);
                             return new GraphNode<>(rootNode, hierarchyProvider.isLeaf(rootEntity));
                         })
                         .sorted(comparing(node -> node.getUserObject().getBrowserText()))
                         .collect(toList());
            return new GetHierarchyRootsResult(rootNodes);
        }).orElse(new GetHierarchyRootsResult(Collections.emptyList()));
    }
}
