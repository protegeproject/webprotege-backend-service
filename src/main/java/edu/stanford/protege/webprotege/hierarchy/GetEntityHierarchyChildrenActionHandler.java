package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.common.Page;
import edu.stanford.protege.webprotege.common.PageCollector;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.entity.EntityNode;
import edu.stanford.protege.webprotege.hierarchy.ordering.ProjectOrderedChildren;
import edu.stanford.protege.webprotege.hierarchy.ordering.ProjectOrderedChildrenRepository;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.mansyntax.render.DeprecatedEntityChecker;
import edu.stanford.protege.webprotege.shortform.DictionaryManager;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import jakarta.inject.Inject;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static edu.stanford.protege.webprotege.access.BuiltInAction.VIEW_PROJECT;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 28 Nov 2017
 */
public class GetEntityHierarchyChildrenActionHandler extends AbstractProjectActionHandler<GetHierarchyChildrenAction, GetHierarchyChildrenResult> {

    @Nonnull
    private final HierarchyProviderManager hierarchyProviderManager;

    @Nonnull
    private final DeprecatedEntityChecker deprecatedEntityChecker;

    @Nonnull
    private final GraphNodeRenderer nodeRenderer;

    @Nonnull
    private final DictionaryManager dictionaryManager;

    @Nonnull
    private final ProjectOrderedChildrenRepository repository;

    @Inject
    public GetEntityHierarchyChildrenActionHandler(@Nonnull AccessManager accessManager,
                                                   @Nonnull HierarchyProviderManager hierarchyProviderManager,
                                                   @Nonnull DeprecatedEntityChecker deprecatedEntityChecker,
                                                   @Nonnull GraphNodeRenderer nodeRenderer,
                                                   @Nonnull DictionaryManager dictionaryManager,
                                                   @Nonnull ProjectOrderedChildrenRepository repository) {
        super(accessManager);
        this.hierarchyProviderManager = hierarchyProviderManager;
        this.deprecatedEntityChecker = deprecatedEntityChecker;
        this.nodeRenderer = nodeRenderer;
        this.dictionaryManager = dictionaryManager;
        this.repository = repository;
    }

    static GetHierarchyChildrenResult emptyResult() {
        return new GetHierarchyChildrenResult(null, Page.emptyPage());
    }

    @Nonnull
    @Override
    public Class<GetHierarchyChildrenAction> getActionClass() {
        return GetHierarchyChildrenAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetHierarchyChildrenAction action) {
        return VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public GetHierarchyChildrenResult execute(@Nonnull GetHierarchyChildrenAction action, @Nonnull ExecutionContext executionContext) {
        var hierarchyDescriptor = action.hierarchyDescriptor();
        Optional<HierarchyProvider<OWLEntity>> hierarchyProvider = hierarchyProviderManager.getHierarchyProvider(hierarchyDescriptor);
        if (hierarchyProvider.isEmpty()) {
            return emptyResult();
        }

        OWLEntity parent = action.entity();
        GraphNode parentNode = nodeRenderer.toGraphNode(parent, hierarchyProvider.get());

        Optional<ProjectOrderedChildren> orderedChildren = repository.findOrderedChildren(action.projectId(), parent.toStringID());

        List<String> orderedEntityUris = orderedChildren.map(ProjectOrderedChildren::children).orElse(Collections.emptyList());

        Page<GraphNode<EntityNode>> page = hierarchyProvider.get().getChildren(parent).stream()
                // Filter out deprecated entities that are displayed under owl:Thing, owl:topObjectProperty
                // owl:topDataProperty
                .filter(child -> isNotDeprecatedTopLevelEntity(parent, child))
                .sorted(comparingUsingOrderList(orderedEntityUris))
                .collect(PageCollector.toPage(action.pageRequest().getPageNumber(), 2000))
                .map(pg -> pg.transform(child -> nodeRenderer.toGraphNode(child, hierarchyProvider.get())))
                .orElse(Page.emptyPage());

        return new GetHierarchyChildrenResult(parentNode, page);
    }

    private Comparator<OWLEntity> comparingShortFormIgnoringCase() {
        return (o1, o2) -> {
            var s1 = dictionaryManager.getShortForm(o1);
            var s2 = dictionaryManager.getShortForm(o2);
            return s1.compareToIgnoreCase(s2);
        };
    }

    private Comparator<OWLEntity> comparingUsingOrderList(List<String> orderList) {
        return Comparator.comparingInt((OWLEntity child) -> {
            String iri = child.toStringID();
            int index = orderList.indexOf(iri);
            return index == -1 ? Integer.MAX_VALUE : index;
        }).thenComparing(comparingShortFormIgnoringCase());
    }

    private boolean isNotDeprecatedTopLevelEntity(OWLEntity parent, OWLEntity child) {
        return !(parent.isTopEntity() && deprecatedEntityChecker.isDeprecated(child));
    }
}
