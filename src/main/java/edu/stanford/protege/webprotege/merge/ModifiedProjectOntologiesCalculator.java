package edu.stanford.protege.webprotege.merge;



import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.project.Ontology;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/01/15
 */
public class ModifiedProjectOntologiesCalculator {

    private final ImmutableSet<Ontology> projectOntologies;

    private final ImmutableSet<Ontology> editedOntologies;

    private final OntologyDiffCalculator diffCalculator;


    @Inject
    public ModifiedProjectOntologiesCalculator(@Nonnull Collection<Ontology> projectOntologies,
                                               @Nonnull Collection<Ontology> editedOntologies,
                                               @Nonnull OntologyDiffCalculator diffCalculator) {
        this.projectOntologies = ImmutableSet.copyOf(checkNotNull(projectOntologies));
        this.editedOntologies = ImmutableSet.copyOf(checkNotNull(editedOntologies));
        this.diffCalculator = checkNotNull(diffCalculator);
    }

    @Nonnull
    public Set<OntologyDiff> getModifiedOntologyDiffs() {
        Set<OntologyDiff> diffs = new HashSet<>();
        for(Ontology projectOntology : projectOntologies) {
            for(Ontology editedOntology : editedOntologies) {
                if(isDifferentVersionOfOntology(projectOntology.getOntologyId(), editedOntology.getOntologyId())) {
                    OntologyDiff ontologyDiff = diffCalculator.computeDiff(projectOntology, editedOntology);
                    if (!ontologyDiff.isEmpty()) {
                        diffs.add(ontologyDiff);
                    }
                }
            }
        }
        return diffs;
    }


    private boolean isDifferentVersionOfOntology(OWLOntologyID ontologyId, OWLOntologyID otherOntologyId) {
        if(ontologyId.isAnonymous()) {
            return false;
        }
        if(otherOntologyId.isAnonymous()) {
            return false;
        }
        Optional<IRI> ontologyIRI = ontologyId.getOntologyIRI();
        Optional<IRI> otherOntologyIRI = otherOntologyId.getOntologyIRI();
        return ontologyIRI.equals(otherOntologyIRI);
    }
}
