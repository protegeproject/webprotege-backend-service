package edu.stanford.protege.webprotege.index.impl;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.change.AddAxiomChange;
import edu.stanford.protege.webprotege.change.RemoveAxiomChange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.*;
import uk.ac.manchester.cs.owl.owlapi.OWLAnnotationPropertyRangeAxiomImpl;

import java.util.Collections;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-08
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AnnotationAxiomsByIriReferenceIndexImpl_AnnotationPropertyRange_TestCase {

    private AnnotationAxiomsByIriReferenceIndexImpl impl;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private IRI rangeIri, otherRangeIri;

    private OWLAnnotationPropertyRangeAxiom annotationPropertyRangeAxiom, otherAnnotationPropertyRangeAxiom;

    @Mock
    private OWLAnnotationProperty property;

    @Mock
    private OWLAnnotation axiomAnnotation;

    @Mock
    private IRI axiomAnnotationValue;

    @BeforeEach
    public void setUp() {

        annotationPropertyRangeAxiom = new OWLAnnotationPropertyRangeAxiomImpl(property, rangeIri, axiomAnnotations());
        otherAnnotationPropertyRangeAxiom = new OWLAnnotationPropertyRangeAxiomImpl(property, otherRangeIri, axiomAnnotations());

        impl = new AnnotationAxiomsByIriReferenceIndexImpl();
        impl.applyChanges(ImmutableList.of(AddAxiomChange.of(ontologyId, annotationPropertyRangeAxiom)));
    }

    private Set<OWLAnnotation> axiomAnnotations() {
        when(axiomAnnotation.getValue())
                .thenReturn(axiomAnnotationValue);
        return Collections.singleton(axiomAnnotation);
    }

    @Test
    public void shouldGetAnnotationPropertyRangeAxiomByRangeIri() {
        var axioms = impl.getReferencingAxioms(rangeIri, ontologyId).collect(toSet());
        assertThat(axioms, hasItem(annotationPropertyRangeAxiom));
    }

    @Test
    public void shouldGetAnnotationPropertyRangeAxiomByAxiomAnnotationValue() {
        var axioms = impl.getReferencingAxioms(axiomAnnotationValue, ontologyId).collect(toSet());
        assertThat(axioms, hasItem(annotationPropertyRangeAxiom));
    }

    @Test
    public void shouldHandleAddAnnotationPropertyRangeAxiom() {
        var addAxiom = AddAxiomChange.of(ontologyId, otherAnnotationPropertyRangeAxiom);
        impl.applyChanges(ImmutableList.of(addAxiom));

        var axioms = impl.getReferencingAxioms(otherRangeIri, ontologyId).collect(toSet());
        assertThat(axioms, hasItems(otherAnnotationPropertyRangeAxiom));
    }

    @Test
    public void shouldHandleRemoveAnnotationPropertyRangeAxiom() {
        var removeAxiom = RemoveAxiomChange.of(ontologyId, annotationPropertyRangeAxiom);
        impl.applyChanges(ImmutableList.of(removeAxiom));

        var axioms = impl.getReferencingAxioms(rangeIri, ontologyId).collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }
}
