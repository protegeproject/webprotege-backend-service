package edu.stanford.protege.webprotege.index.impl;

import edu.stanford.protege.webprotege.index.AnnotationAssertionAxiomsBySubjectIndex;
import edu.stanford.protege.webprotege.index.DataPropertyAssertionAxiomsBySubjectIndex;
import edu.stanford.protege.webprotege.index.ObjectPropertyAssertionAxiomsBySubjectIndex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.*;

import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-12
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PropertyAssertionAxiomsBySubjectIndexImpl_TestCase {

    private PropertyAssertionAxiomsBySubjectIndexImpl impl;

    @Mock
    private AnnotationAssertionAxiomsBySubjectIndex annotationAssertionAxiomsBySubject;

    @Mock
    private ObjectPropertyAssertionAxiomsBySubjectIndex objectPropertyAssertionAxiomsBySubject;

    @Mock
    private DataPropertyAssertionAxiomsBySubjectIndex dataPropertyAssertionAxiomsBySubject;

    @Mock
    private OWLNamedIndividual individual;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLAnnotationAssertionAxiom annotationAssertion;

    @Mock
    private OWLObjectPropertyAssertionAxiom objectPropertyAssertion;

    @Mock
    private OWLDataPropertyAssertionAxiom dataPropertyAssertion;

    @Mock
    private IRI iri;

    @BeforeEach
    public void setUp() {
        when(individual.getIRI()).thenReturn(iri);

        when(annotationAssertionAxiomsBySubject.getAxiomsForSubject(any(), any()))
                .thenReturn(Stream.empty());
        when(annotationAssertionAxiomsBySubject.getAxiomsForSubject(iri, ontologyId))
                .thenReturn(Stream.of(annotationAssertion));

        when(objectPropertyAssertionAxiomsBySubject.getObjectPropertyAssertions(any(), any()))
                .thenReturn(Stream.empty());
        when(objectPropertyAssertionAxiomsBySubject.getObjectPropertyAssertions(individual, ontologyId))
                .thenReturn(Stream.of(objectPropertyAssertion));

        when(dataPropertyAssertionAxiomsBySubject.getDataPropertyAssertions(any(), any()))
                .thenReturn(Stream.empty());

        when(dataPropertyAssertionAxiomsBySubject.getDataPropertyAssertions(individual, ontologyId))
                .thenReturn(Stream.of(dataPropertyAssertion));
        impl = new PropertyAssertionAxiomsBySubjectIndexImpl(annotationAssertionAxiomsBySubject,
                                                             objectPropertyAssertionAxiomsBySubject,
                                                             dataPropertyAssertionAxiomsBySubject);
    }

    @Test
    public void shouldGetDependencies() {
        assertThat(impl.getDependencies(), containsInAnyOrder(annotationAssertionAxiomsBySubject,
                                                              dataPropertyAssertionAxiomsBySubject,
                                                              objectPropertyAssertionAxiomsBySubject));
    }

    @Test
    public void shouldGetAssertionsForNamedIndividual() {
        var axioms = impl.getPropertyAssertions(individual, ontologyId).collect(toSet());
        assertThat(axioms, containsInAnyOrder(annotationAssertion, objectPropertyAssertion, dataPropertyAssertion));
    }

    @Test
    public void shouldGetEmptyForUnknownSubject() {
        var axioms = impl.getPropertyAssertions(mock(OWLNamedIndividual.class), ontologyId).collect(toSet());
        assertThat(axioms, is(empty()));
    }

    @Test
    public void shouldGetEmptyForUnknownOntologyId() {
        var axioms = impl.getPropertyAssertions(individual, mock(OWLOntologyID.class)).collect(toSet());
        assertThat(axioms, is(empty()));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNpeIfSubjectIsNull() {
    assertThrows(NullPointerException.class, () -> { 
        impl.getPropertyAssertions(null, ontologyId);
     });
}

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNpeIfOntologyIdIsNull() {
    assertThrows(NullPointerException.class, () -> { 
        impl.getPropertyAssertions(individual, null);
     });
}
}
