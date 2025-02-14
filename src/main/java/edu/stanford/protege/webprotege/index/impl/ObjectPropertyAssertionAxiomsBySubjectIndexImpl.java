package edu.stanford.protege.webprotege.index.impl;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.change.OntologyChange;
import edu.stanford.protege.webprotege.index.ObjectPropertyAssertionAxiomsBySubjectIndex;
import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLPropertyAssertionAxiom;

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
public class ObjectPropertyAssertionAxiomsBySubjectIndexImpl implements ObjectPropertyAssertionAxiomsBySubjectIndex, UpdatableIndex {

    @Nonnull
    private final AxiomMultimapIndex<OWLIndividual, OWLObjectPropertyAssertionAxiom> index;

    @Inject
    public ObjectPropertyAssertionAxiomsBySubjectIndexImpl() {
        this.index = AxiomMultimapIndex.create(OWLObjectPropertyAssertionAxiom.class,
                                               OWLPropertyAssertionAxiom::getSubject);
    }
    @Override
    public void applyChanges(@Nonnull ImmutableList<OntologyChange> changes) {
        index.applyChanges(changes);
    }

    @Nonnull
    @Override
    public Stream<OWLObjectPropertyAssertionAxiom> getObjectPropertyAssertions(@Nonnull OWLIndividual subject,
                                                                               @Nonnull OWLOntologyID ontologyId) {
        checkNotNull(subject);
        checkNotNull(ontologyId);
        return index.getAxioms(subject, ontologyId);
    }
}
