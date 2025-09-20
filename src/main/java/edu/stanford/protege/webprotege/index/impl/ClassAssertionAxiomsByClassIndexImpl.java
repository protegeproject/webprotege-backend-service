package edu.stanford.protege.webprotege.index.impl;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.change.OntologyChange;
import edu.stanford.protege.webprotege.index.ClassAssertionAxiomsByClassIndex;
import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-19
 */
@ProjectSingleton
public class ClassAssertionAxiomsByClassIndexImpl implements ClassAssertionAxiomsByClassIndex, UpdatableIndex {

    @Nonnull
    private final AxiomMultimapIndex<OWLClassExpression, OWLClassAssertionAxiom> index;

    @Inject
    public ClassAssertionAxiomsByClassIndexImpl() {
        index = AxiomMultimapIndex.create(OWLClassAssertionAxiom.class,
                                          OWLClassAssertionAxiom::getClassExpression);
        index.setLazy(true);
    }

    @Nonnull
    @Override
    public Stream<OWLClassAssertionAxiom> getClassAssertionAxioms(@Nonnull OWLClass cls,
                                                                  @Nonnull OWLOntologyID ontologyId) {
        checkNotNull(ontologyId);
        checkNotNull(cls);
        return index.getAxioms(cls, ontologyId);
    }

    @Override
    public void applyChanges(@Nonnull ImmutableList<OntologyChange> changes) {
        index.applyChanges(changes);
    }

    @Override
    public void reset() {
        index.clear();
    }
}
