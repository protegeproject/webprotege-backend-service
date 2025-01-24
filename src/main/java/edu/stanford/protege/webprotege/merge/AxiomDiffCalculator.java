package edu.stanford.protege.webprotege.merge;

import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.project.Ontology;
import org.semanticweb.owlapi.model.OWLAxiom;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/01/15
 */
public class AxiomDiffCalculator {

    @Inject
    public AxiomDiffCalculator() {
    }

    public Diff<OWLAxiom> computeDiff(@Nonnull Ontology from,
                                      @Nonnull Ontology to) {
        var fromAxioms = checkNotNull(from).getAxioms();
        var toAxioms = checkNotNull(to).getAxioms();
        var addedAxioms = ImmutableSet.<OWLAxiom>builder();
        var removedAxioms = ImmutableSet.<OWLAxiom>builder();
        for(var toAx : to.getAxioms()) {
            if(!fromAxioms.contains(toAx)) {
                addedAxioms.add(toAx);
            }
        }
        for(var fromAx : from.getAxioms()) {
            if(!toAxioms.contains(fromAx)) {
                removedAxioms.add(fromAx);
            }
        }
        return new Diff<>(addedAxioms.build(), removedAxioms.build());
    }
}
