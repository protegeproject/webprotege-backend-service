package edu.stanford.protege.webprotege.icd.actions;

import edu.stanford.protege.webprotege.DataFactory;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.hierarchy.ClassHierarchyDescriptor;
import edu.stanford.protege.webprotege.hierarchy.HierarchyProvider;
import edu.stanford.protege.webprotege.hierarchy.HierarchyProviderManager;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.*;

public class ValidateAxisBelongsToHierarchyActionHandler extends AbstractProjectActionHandler<ValidateAxisBelongsToHierarchyAction, ValidateAxisBelongsToHierarchyResult> {

    private static final Logger logger = LoggerFactory.getLogger(ValidateAxisBelongsToHierarchyActionHandler.class);

    private final HierarchyProviderManager hierarchyProviderManager;

    public ValidateAxisBelongsToHierarchyActionHandler(
            @Nonnull AccessManager accessManager,
            @Nonnull HierarchyProviderManager hierarchyProviderManager) {
        super(accessManager);
        this.hierarchyProviderManager = hierarchyProviderManager;
    }

    @Nonnull
    @Override
    public Class<ValidateAxisBelongsToHierarchyAction> getActionClass() {
        return ValidateAxisBelongsToHierarchyAction.class;
    }

    @Nonnull
    @Override
    public ValidateAxisBelongsToHierarchyResult execute(@Nonnull ValidateAxisBelongsToHierarchyAction action, @Nonnull ExecutionContext executionContext) {
        Map<IRI, List<IRI>> invalidEntitiesByRoot = new HashMap<>();

        for (Map.Entry<IRI, List<IRI>> entry : action.hierarchyRootsToEntities().entrySet()) {
            IRI rootIri = entry.getKey();
            List<IRI> entitiesToCheck = entry.getValue();

            if (entitiesToCheck == null || entitiesToCheck.isEmpty()) {
                continue;
            }

            try {
                OWLClass rootClass = DataFactory.getOWLClass(rootIri);
                ClassHierarchyDescriptor hierarchyDescriptor = ClassHierarchyDescriptor.create(Set.of(rootClass));
                
                Optional<HierarchyProvider<OWLEntity>> hierarchyProviderOpt = hierarchyProviderManager.getHierarchyProvider(hierarchyDescriptor);
                
                if (hierarchyProviderOpt.isEmpty()) {
                    // If we can't get a hierarchy provider, consider all entities as invalid
                    invalidEntitiesByRoot.put(rootIri, new ArrayList<>(entitiesToCheck));
                    continue;
                }

                HierarchyProvider<OWLEntity> hierarchyProvider = hierarchyProviderOpt.get();
                
                // Get all descendants of the root class
                Collection<OWLEntity> descendants = hierarchyProvider.getDescendants(rootClass);
                
                // Also include the root class itself in the set of valid entities
                Set<OWLEntity> validEntities = new HashSet<>(descendants);
                validEntities.add(rootClass);
                
                // Check which entities from the list don't exist in the hierarchy
                List<IRI> invalidEntities = new ArrayList<>();
                for (IRI entityIri : entitiesToCheck) {
                    OWLClass entityClass = DataFactory.getOWLClass(entityIri);
                    if (!validEntities.contains(entityClass)) {
                        invalidEntities.add(entityIri);
                    }
                }
                
                // Only add to result if there are invalid entities
                if (!invalidEntities.isEmpty()) {
                    invalidEntitiesByRoot.put(rootIri, invalidEntities);
                }
            } catch (Exception e) {
                // If there's an error processing this root, consider all entities as invalid
                logger.error("Error validating entities for hierarchy root: " + rootIri, e);
                invalidEntitiesByRoot.put(rootIri, new ArrayList<>(entitiesToCheck));
            }
        }

        return ValidateAxisBelongsToHierarchyResult.create(invalidEntitiesByRoot);
    }
}
