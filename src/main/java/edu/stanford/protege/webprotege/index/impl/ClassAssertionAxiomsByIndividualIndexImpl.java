package edu.stanford.protege.webprotege.index.impl;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.change.OntologyChange;
import edu.stanford.protege.webprotege.index.ClassAssertionAxiomsByIndividualIndex;
import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-10
 */
@ProjectSingleton
public class ClassAssertionAxiomsByIndividualIndexImpl implements ClassAssertionAxiomsByIndividualIndex, UpdatableIndex {

    @Nonnull
    private final AxiomMultimapIndex<OWLIndividual, OWLClassAssertionAxiom> index;

    @Inject
    public ClassAssertionAxiomsByIndividualIndexImpl() {
        index = AxiomMultimapIndex.create(OWLClassAssertionAxiom.class,
                                          OWLClassAssertionAxiom::getIndividual);

        index.setLazy(true);
    }

    @Override
    public Stream<OWLClassAssertionAxiom> getClassAssertionAxioms(@Nonnull OWLIndividual individual,
                                                                  @Nonnull OWLOntologyID ontologyID) {
        checkNotNull(individual);
        checkNotNull(ontologyID);
        return index.getAxioms(individual, ontologyID);
    }

    @Override
    public void applyChanges(@Nonnull ImmutableList<OntologyChange> changes) {
        index.applyChanges(changes);
    }
}
