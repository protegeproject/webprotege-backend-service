package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.change.OntologyChange;
import edu.stanford.protege.webprotege.events.HierarchyChangesComputerFactory;
import org.jetbrains.annotations.NotNull;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 4 Dec 2017
 */
public class HierarchyProviderManager {

    private final Map<HierarchyDescriptor, HierarchyProvider<? extends OWLEntity>> hierarchyProviderMap = new HashMap<>();

    @Nonnull
    private final HierarchyProviderFactory hierarchyProviderFactory;

    @Inject
    public HierarchyProviderManager(@Nonnull HierarchyProviderFactory hierarchyProviderFactory) {
        this.hierarchyProviderFactory = hierarchyProviderFactory;
    }

    @SuppressWarnings("unchecked")
    public synchronized Optional<HierarchyProvider<OWLEntity>> getHierarchyProvider(@Nonnull HierarchyDescriptor hierarchyDescriptor) {
        var hierarchyProvider = (HierarchyProvider<OWLEntity>) hierarchyProviderMap.get(hierarchyDescriptor);
        if(hierarchyProvider != null) {
            return Optional.of(hierarchyProvider);
        }
        return createAndInstallHierarchyProvider(hierarchyDescriptor);
    }

    private @NotNull Optional<HierarchyProvider<OWLEntity>> createAndInstallHierarchyProvider(HierarchyDescriptor hierarchyDescriptor) {
        var hierarchyProvider =  hierarchyProviderFactory.createHierarchyProvider(hierarchyDescriptor);
        hierarchyProvider.ifPresent(hp -> {
            hierarchyProviderMap.put(hierarchyDescriptor, hp);
        });
        return hierarchyProvider;
    }

    public void handleChanges(List<OntologyChange> changes) {
        hierarchyProviderMap.values()
                .forEach(hierarchyProvider -> {
                    hierarchyProvider.handleChanges(changes);
                });
    }
}
