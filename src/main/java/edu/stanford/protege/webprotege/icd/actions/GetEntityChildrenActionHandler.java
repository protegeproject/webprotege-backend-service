package edu.stanford.protege.webprotege.icd.actions;

import edu.stanford.protege.webprotege.DataFactory;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.hierarchy.*;
import edu.stanford.protege.webprotege.hierarchy.ordering.ProjectOrderedChildren;
import edu.stanford.protege.webprotege.hierarchy.ordering.ProjectOrderedChildrenService;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.mansyntax.render.DeprecatedEntityChecker;
import edu.stanford.protege.webprotege.shortform.DictionaryManager;
import org.jetbrains.annotations.NotNull;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import java.util.*;

public class GetEntityChildrenActionHandler extends AbstractProjectActionHandler<GetEntityChildrenAction, GetEntityChildrenResult> {

    private final HierarchyProviderManager hierarchyProviderManager;

    @Nonnull
    private final DeprecatedEntityChecker deprecatedEntityChecker;

    @Nonnull
    private final DictionaryManager dictionaryManager;

    @Nonnull
    private final ProjectOrderedChildrenService projectOrderedChildrenService;

    public GetEntityChildrenActionHandler(@Nonnull AccessManager accessManager,
                                          @Nonnull HierarchyProviderManager hierarchyProviderManager,
                                          @Nonnull DeprecatedEntityChecker deprecatedEntityChecker,
                                          @Nonnull DictionaryManager dictionaryManager,
                                          @Nonnull ProjectOrderedChildrenService projectOrderedChildrenService) {
        super(accessManager);

        this.hierarchyProviderManager = hierarchyProviderManager;
        this.deprecatedEntityChecker = deprecatedEntityChecker;
        this.dictionaryManager = dictionaryManager;
        this.projectOrderedChildrenService = projectOrderedChildrenService;
    }

    @NotNull
    @Override
    public Class<GetEntityChildrenAction> getActionClass() {
        return GetEntityChildrenAction.class;
    }

    @NotNull
    @Override
    public GetEntityChildrenResult execute(@NotNull GetEntityChildrenAction action, @NotNull ExecutionContext executionContext) {

        var parentClass = DataFactory.getOWLClass(action.classIri());
        Optional<HierarchyProvider<OWLEntity>> hierarchyProvider = hierarchyProviderManager.getHierarchyProvider(ClassHierarchyDescriptor.create());
        if (hierarchyProvider.isEmpty()) {
            return emptyResult();
        }

        var orderedChildren = projectOrderedChildrenService.findOrderedChildren(action.projectId(), action.classIri());
        List<String> orderedEntityUris = orderedChildren.map(ProjectOrderedChildren::children).orElse(Collections.emptyList());

        List<IRI> children = hierarchyProvider.get().getChildren(parentClass).stream()
                // Filter out deprecated entities that are displayed under owl:Thing, owl:topObjectProperty
                // owl:topDataProperty
                .filter(child -> isNotDeprecatedTopLevelEntity(parentClass, child))
                .sorted(comparingUsingOrderList(orderedEntityUris))
                .map(OWLEntity::getIRI)
                .toList();

        return GetEntityChildrenResult.create(children);
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
            String iri = child.getIRI().toString();
            int index = orderList.indexOf(iri);
            return index == -1 ? Integer.MAX_VALUE : index;
        }).thenComparing(comparingShortFormIgnoringCase());
    }

    private boolean isNotDeprecatedTopLevelEntity(OWLEntity parent, OWLEntity child) {
        return !(parent.isTopEntity() && deprecatedEntityChecker.isDeprecated(child));
    }

    static GetEntityChildrenResult emptyResult() {
        return GetEntityChildrenResult.create(List.of());
    }
}
