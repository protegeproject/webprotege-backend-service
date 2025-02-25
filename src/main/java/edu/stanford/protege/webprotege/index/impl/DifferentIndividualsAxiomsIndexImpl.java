package edu.stanford.protege.webprotege.index.impl;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.change.OntologyChange;
import edu.stanford.protege.webprotege.index.DifferentIndividualsAxiomsIndex;
import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-24
 */
@ProjectSingleton
public class DifferentIndividualsAxiomsIndexImpl implements DifferentIndividualsAxiomsIndex, UpdatableIndex {

    @Nonnull
    private final AxiomMultimapIndex<OWLIndividual, OWLDifferentIndividualsAxiom> index;

    @Inject
    public DifferentIndividualsAxiomsIndexImpl() {
        this.index = AxiomMultimapIndex.createWithNaryKeyValueExtractor(
                OWLDifferentIndividualsAxiom.class,
                OWLDifferentIndividualsAxiom::getIndividuals);
    }

    @Nonnull
    @Override
    public Stream<OWLDifferentIndividualsAxiom> getDifferentIndividualsAxioms(@Nonnull OWLIndividual individual,
                                                                              @Nonnull OWLOntologyID ontologyId) {
        checkNotNull(ontologyId);
        checkNotNull(individual);
        return index.getAxioms(individual, ontologyId);
    }

    @Override
    public void applyChanges(@Nonnull ImmutableList<OntologyChange> changes) {
        index.applyChanges(changes);
    }
}
