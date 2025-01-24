package edu.stanford.protege.webprotege.index.impl;

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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-15
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class OntologySignatureIndexImpl_TestCase {

    private OntologySignatureIndexImpl impl;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private AxiomsByEntityReferenceIndexImpl axiomsByEntityReferenceImpl;

    @Mock
    private OWLClass cls;

    @Mock
    private OWLObjectProperty objectProperty;

    @Mock
    private OWLDataProperty dataProperty;

    @Mock
    private OWLAnnotationProperty annotationProperty;

    @Mock
    private OWLNamedIndividual individual;

    @Mock
    private OWLDatatype datatype;


    @BeforeEach
    public void setUp() {
        when(axiomsByEntityReferenceImpl.getOntologyAxiomsSignature(EntityType.CLASS, ontologyId))
                .thenAnswer(invocation -> Stream.of(cls));
        when(axiomsByEntityReferenceImpl.getOntologyAxiomsSignature(EntityType.OBJECT_PROPERTY, ontologyId))
                .thenAnswer(invocation -> Stream.of(objectProperty));
        when(axiomsByEntityReferenceImpl.getOntologyAxiomsSignature(EntityType.DATA_PROPERTY, ontologyId))
                .thenAnswer(invocation -> Stream.of(dataProperty));
        when(axiomsByEntityReferenceImpl.getOntologyAxiomsSignature(EntityType.ANNOTATION_PROPERTY, ontologyId))
                .thenAnswer(invocation -> Stream.of(annotationProperty));
        when(axiomsByEntityReferenceImpl.getOntologyAxiomsSignature(EntityType.NAMED_INDIVIDUAL, ontologyId))
                .thenAnswer(invocation -> Stream.of(individual));
        when(axiomsByEntityReferenceImpl.getOntologyAxiomsSignature(EntityType.DATATYPE, ontologyId))
                .thenAnswer(invocation -> Stream.of(datatype));
        impl = new OntologySignatureIndexImpl(axiomsByEntityReferenceImpl);
    }

    @Test
    public void shouldGetDependencies() {
        assertThat(impl.getDependencies(), contains(axiomsByEntityReferenceImpl));
    }

    @Test
    public void shouldGetSignatureOfKnownOntology() {
        var signature = impl.getEntitiesInSignature(ontologyId).collect(toSet());
        assertThat(signature, hasItems(cls, objectProperty, dataProperty, annotationProperty, individual, datatype));
    }

    @Test
    public void shouldGetEmptyStreamForUnknownOntology() {
        var signature = impl.getEntitiesInSignature(mock(OWLOntologyID.class)).collect(toSet());
        assertThat(signature.isEmpty(), is(true));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNpeIfOntologyIdIsNull() {
    assertThrows(NullPointerException.class, () -> { 
        impl.getEntitiesInSignature(null);
     });
}
}
