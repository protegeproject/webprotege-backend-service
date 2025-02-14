package edu.stanford.protege.webprotege.index.impl;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.change.OntologyChange;
import edu.stanford.protege.webprotege.index.DataPropertyAssertionAxiomsBySubjectIndex;
import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-12
 */
@ProjectSingleton
public class DataPropertyAssertionAxiomsBySubjectIndexImpl implements DataPropertyAssertionAxiomsBySubjectIndex,
        UpdatableIndex {

    @Nonnull
    private final AxiomMultimapIndex<OWLIndividual, OWLDataPropertyAssertionAxiom> index;

    @Inject
    public DataPropertyAssertionAxiomsBySubjectIndexImpl() {
        index = AxiomMultimapIndex.create(OWLDataPropertyAssertionAxiom.class,
                                          OWLDataPropertyAssertionAxiom::getSubject);
    }

    @Nonnull
    @Override
    public Stream<OWLDataPropertyAssertionAxiom> getDataPropertyAssertions(@Nonnull OWLIndividual individual,
                                                                           @Nonnull OWLOntologyID ontologyId) {
        checkNotNull(individual);
        checkNotNull(ontologyId);
        return index.getAxioms(individual, ontologyId);
    }

    @Override
    public void applyChanges(@Nonnull ImmutableList<OntologyChange> changes) {
        index.applyChanges(changes);
    }
}
