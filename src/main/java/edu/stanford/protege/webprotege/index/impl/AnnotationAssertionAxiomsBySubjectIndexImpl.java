package edu.stanford.protege.webprotege.index.impl;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.change.OntologyChange;
import edu.stanford.protege.webprotege.index.AnnotationAssertionAxiomsBySubjectIndex;
import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationSubject;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-09
 */
@ProjectSingleton
public class AnnotationAssertionAxiomsBySubjectIndexImpl implements AnnotationAssertionAxiomsBySubjectIndex, UpdatableIndex {

    @Nonnull
    private final AxiomMultimapIndex<OWLAnnotationSubject, OWLAnnotationAssertionAxiom> index;

    @Inject
    public AnnotationAssertionAxiomsBySubjectIndexImpl() {
        index = AxiomMultimapIndex.create(OWLAnnotationAssertionAxiom.class,
                                          OWLAnnotationAssertionAxiom::getSubject);
    }

    @Override
    public Stream<OWLAnnotationAssertionAxiom> getAxiomsForSubject(@Nonnull OWLAnnotationSubject subject,
                                                                   @Nonnull OWLOntologyID ontologyId) {
        checkNotNull(subject);
        checkNotNull(ontologyId);
        return index.getAxioms(subject, ontologyId);
    }

    @Override
    public void applyChanges(@Nonnull ImmutableList<OntologyChange> changes) {
        index.applyChanges(changes);
    }
}
