package edu.stanford.protege.webprotege.icd.actions;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.hierarchy.*;
import edu.stanford.protege.webprotege.index.EntitiesInProjectSignatureByIriIndex;
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
    private final EntitiesInProjectSignatureByIriIndex entitiesInProjectSignatureByIriIndex;


    public GetEntityChildrenActionHandler(@Nonnull AccessManager accessManager,
                                          @Nonnull HierarchyProviderManager hierarchyProviderManager,
                                          @Nonnull DeprecatedEntityChecker deprecatedEntityChecker,
                                          @Nonnull DictionaryManager dictionaryManager,
                                          @Nonnull EntitiesInProjectSignatureByIriIndex entitiesInProjectSignatureByIriIndex) {
        super(accessManager);

        this.hierarchyProviderManager = hierarchyProviderManager;
        this.deprecatedEntityChecker = deprecatedEntityChecker;
        this.dictionaryManager = dictionaryManager;
        this.entitiesInProjectSignatureByIriIndex = entitiesInProjectSignatureByIriIndex;
    }

    @NotNull
    @Override
    public Class<GetEntityChildrenAction> getActionClass() {
        return GetEntityChildrenAction.class;
    }

    @NotNull
    @Override
    public GetEntityChildrenResult execute(@NotNull GetEntityChildrenAction action, @NotNull ExecutionContext executionContext) {
        var hierarchyDescriptor = action.hierarchyDescriptor();
        var parentOptional = entitiesInProjectSignatureByIriIndex.getEntitiesInSignature(action.classIri()).findFirst();
        Optional<HierarchyProvider<OWLEntity>> hierarchyProvider = hierarchyProviderManager.getHierarchyProvider(hierarchyDescriptor);
        if (hierarchyProvider.isEmpty()) {
            return emptyResult();
        }

        if (parentOptional.isPresent()) {
            var parent = parentOptional.get();

            List<IRI> children = hierarchyProvider.get().getChildren(parent).stream()
                    // Filter out deprecated entities that are displayed under owl:Thing, owl:topObjectProperty
                    // owl:topDataProperty
                    .filter(child -> isNotDeprecatedTopLevelEntity(parent, child))
                    .sorted(comparingShortFormIgnoringCase())
                    .map(OWLEntity::getIRI)
                    .toList();


            return GetEntityChildrenResult.create(children);
        }

        return emptyResult();
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

    static GetEntityChildrenResult emptyResult() {
        return GetEntityChildrenResult.create(List.of());
    }
}
