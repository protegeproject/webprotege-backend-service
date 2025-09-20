package edu.stanford.protege.webprotege.index.impl;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.change.OntologyChange;
import edu.stanford.protege.webprotege.index.DisjointClassesAxiomsIndex;
import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-22
 */
@ProjectSingleton
public class DisjointClassesAxiomsIndexImpl implements DisjointClassesAxiomsIndex, UpdatableIndex {

    @Nonnull
    private final AxiomMultimapIndex<OWLClassExpression, OWLDisjointClassesAxiom> index;

    @Inject
    public DisjointClassesAxiomsIndexImpl() {
        index = AxiomMultimapIndex.createWithNaryKeyValueExtractor(OWLDisjointClassesAxiom.class,
                                                                   OWLDisjointClassesAxiom::getClassExpressions);

        index.setLazy(true);
    }

    @Nonnull
    @Override
    public Stream<OWLDisjointClassesAxiom> getDisjointClassesAxioms(@Nonnull OWLClass cls, OWLOntologyID ontologyId) {
        checkNotNull(cls);
        checkNotNull(ontologyId);
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
