package edu.stanford.protege.webprotege.change.matcher;

import edu.stanford.protege.webprotege.change.OntologyChange;
import edu.stanford.protege.webprotege.change.RemoveAxiomChange;
import edu.stanford.protege.webprotege.change.description.DeletedEntities;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2018-12-04
 */
public class EntityDeletionMatcher implements ChangeMatcher {

    @Inject
    public EntityDeletionMatcher() {
    }

    @Override
    public Optional<ChangeSummary> getDescription(List<OntologyChange> changes) {
        // All changes must be removes
        var nonRemovalChange = changes.stream()
                .anyMatch(EntityDeletionMatcher::isNonRemovalChange);
        if(nonRemovalChange) {
            return Optional.empty();
        }
        // Must be entity declarations that are removed
        var removedEntities = changes
                .stream()
                .filter(OntologyChange::isRemoveAxiom)
                .map(data -> ((RemoveAxiomChange) data).getAxiom())
                .filter(ax -> ax instanceof OWLDeclarationAxiom)
                .map(ax -> ((OWLDeclarationAxiom) ax).getEntity())
                .collect(toImmutableSet());
        if(removedEntities.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(ChangeSummary.get(DeletedEntities.get(removedEntities)));
    }

    private static boolean isNonRemovalChange(OntologyChange change) {
        return !(change.isRemoveAxiom() || change.isRemoveOntologyAnnotation());
    }
}
