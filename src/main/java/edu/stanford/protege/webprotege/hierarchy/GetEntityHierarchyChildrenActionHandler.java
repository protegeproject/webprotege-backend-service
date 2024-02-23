package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.common.Page;
import edu.stanford.protege.webprotege.common.PageCollector;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.entity.EntityNode;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.mansyntax.render.DeprecatedEntityChecker;
import edu.stanford.protege.webprotege.shortform.DictionaryManager;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Comparator;
import java.util.Optional;

import static edu.stanford.protege.webprotege.access.BuiltInAction.VIEW_PROJECT;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 28 Nov 2017
 */
public class GetEntityHierarchyChildrenActionHandler extends AbstractProjectActionHandler<GetHierarchyChildrenAction, GetHierarchyChildrenResult> {

    @Nonnull
    private final HierarchyProviderMapper hierarchyProviderMapper;

    @Nonnull
    private final DeprecatedEntityChecker deprecatedEntityChecker;

    @Nonnull
    private final GraphNodeRenderer nodeRenderer;

    @Nonnull
    private final DictionaryManager dictionaryManager;

    @Inject
    public GetEntityHierarchyChildrenActionHandler(@Nonnull AccessManager accessManager,
                                                   @Nonnull HierarchyProviderMapper hierarchyProviderMapper,
                                                   @Nonnull DeprecatedEntityChecker deprecatedEntityChecker,
                                                   @Nonnull GraphNodeRenderer nodeRenderer, @Nonnull DictionaryManager dictionaryManager) {
        super(accessManager);
        this.hierarchyProviderMapper = hierarchyProviderMapper;
        this.deprecatedEntityChecker = deprecatedEntityChecker;
        this.nodeRenderer = nodeRenderer;
        this.dictionaryManager = dictionaryManager;
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
        HierarchyId hierarchyId = action.hierarchyId();
        Optional<HierarchyProvider<OWLEntity>> hierarchyProvider = hierarchyProviderMapper.getHierarchyProvider(hierarchyId);
        if (hierarchyProvider.isEmpty()) {
            return emptyResult();
        }
        OWLEntity parent = action.entity();
        GraphNode parentNode = nodeRenderer.toGraphNode(parent, hierarchyProvider.get());
        Page<GraphNode<EntityNode>> page = hierarchyProvider.get().getChildren(parent).stream()
                         // Filter out deprecated entities that are displayed under owl:Thing, owl:topObjectProperty
                         // owl:topDataProperty
                         .filter(child -> isNotDeprecatedTopLevelEntity(parent, child))
                         .sorted(comparingShortFormIgnoringCase())
                         .collect(PageCollector.toPage(action.pageRequest().getPageNumber(),
                                                       2000))
                         .map(pg ->
                             pg.transform(child -> nodeRenderer.toGraphNode(child, hierarchyProvider.get()))
                         ).orElse(Page.emptyPage());

        return new GetHierarchyChildrenResult(parentNode, page);
    }

    private Comparator<OWLEntity> comparingShortFormIgnoringCase() {
        return (o1, o2) -> {
            var s1 = dictionaryManager.getShortForm(o1);
            var s2 = dictionaryManager.getShortForm(o2);
            return s1.compareToIgnoreCase(s2);
        };
    }

    private boolean isNotDeprecatedTopLevelEntity(OWLEntity parent, OWLEntity child) {
        return !(parent.isTopEntity() && deprecatedEntityChecker.isDeprecated(child));
    }
}
