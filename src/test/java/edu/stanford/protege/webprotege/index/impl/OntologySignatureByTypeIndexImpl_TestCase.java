package edu.stanford.protege.webprotege.index.impl;

import edu.stanford.protege.webprotege.index.OntologyAnnotationsSignatureIndex;
import edu.stanford.protege.webprotege.index.OntologyAxiomsSignatureIndex;
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
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.semanticweb.owlapi.model.EntityType.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-16
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class OntologySignatureByTypeIndexImpl_TestCase {

    private OntologySignatureByTypeIndexImpl impl;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLClass cls;

    @Mock
    private OWLObjectProperty objectProperty;

    @Mock
    private OWLDataProperty dataProperty;

    @Mock
    private OWLAnnotationProperty annotationProperty;

    @Mock
    private OWLAnnotationProperty annotationProperty2;

    @Mock
    private OWLNamedIndividual individual;

    @Mock
    private OWLDatatype datatype;

    @Mock
    private OntologyAxiomsSignatureIndex ontologyAxiomsSignatureIndex;

    @Mock
    private OntologyAnnotationsSignatureIndex ontologyAnnotationsSignatureIndex;

    @BeforeEach
    public void setUp() {
        impl = new OntologySignatureByTypeIndexImpl(ontologyAxiomsSignatureIndex,
                                                    ontologyAnnotationsSignatureIndex);


        when(ontologyAxiomsSignatureIndex.getOntologyAxiomsSignature(any(), any()))
                .thenAnswer(invocation -> Stream.empty());

        when(ontologyAxiomsSignatureIndex.getOntologyAxiomsSignature(CLASS, ontologyId))

                .thenAnswer(invocation -> Stream.of(cls));
        when(ontologyAxiomsSignatureIndex.getOntologyAxiomsSignature(OBJECT_PROPERTY, ontologyId))
                .thenAnswer(invocation -> Stream.of(objectProperty));

        when(ontologyAxiomsSignatureIndex.getOntologyAxiomsSignature(DATA_PROPERTY, ontologyId))
                .thenAnswer(invocation -> Stream.of(dataProperty));

        when(ontologyAxiomsSignatureIndex.getOntologyAxiomsSignature(ANNOTATION_PROPERTY, ontologyId))
                .thenAnswer(invocation -> Stream.of(annotationProperty));

        when(ontologyAnnotationsSignatureIndex.getOntologyAnnotationsSignature(ontologyId))
                .thenAnswer(invocation -> Stream.of(annotationProperty2));

        when(ontologyAxiomsSignatureIndex.getOntologyAxiomsSignature(NAMED_INDIVIDUAL, ontologyId))
                .thenAnswer(invocation -> Stream.of(individual));

        when(ontologyAxiomsSignatureIndex.getOntologyAxiomsSignature(DATATYPE, ontologyId))
                .thenAnswer(invocation -> Stream.of(datatype));
    }

    @Test
    public void shouldGetDependencies() {
        assertThat(impl.getDependencies(), containsInAnyOrder(ontologyAxiomsSignatureIndex,
                                                    ontologyAnnotationsSignatureIndex));
    }

    @Test
    public void shouldGetClassesInSignature() {
        var signature = impl.getSignature(CLASS, ontologyId).collect(toSet());
        assertThat(signature, hasItem(cls));
    }


    @Test
    public void shouldGetDatatypesInSignature() {
        var signature = impl.getSignature(DATATYPE, ontologyId).collect(toSet());
        assertThat(signature, hasItem(datatype));
    }

    @Test
    public void shouldGetObjectPropertiesInSignature() {
        var signature = impl.getSignature(OBJECT_PROPERTY, ontologyId).collect(toSet());
        assertThat(signature, hasItem(objectProperty));
    }

    @Test
    public void shouldGetDataPropertiesInSignature() {
        var signature = impl.getSignature(DATA_PROPERTY, ontologyId).collect(toSet());
        assertThat(signature, hasItem(dataProperty));
    }

    @Test
    public void shouldGetAnnotationPropertiesInSignature() {
        var signature = impl.getSignature(ANNOTATION_PROPERTY, ontologyId).collect(toSet());
        assertThat(signature, hasItems(annotationProperty, annotationProperty2));
    }


    @Test
    public void shouldGetIndividualsInSignature() {
        var signature = impl.getSignature(NAMED_INDIVIDUAL, ontologyId).collect(toSet());
        assertThat(signature, hasItem(individual));
    }

    @Test
    public void shouldGetEmptyStreamForUnknownOntology() {
        var signature = impl.getSignature(CLASS, mock(OWLOntologyID.class)).collect(toSet());
        assertThat(signature.isEmpty(), is(true));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNpeForNullType() {
    assertThrows(NullPointerException.class, () -> { 
        impl.getSignature(null, ontologyId);
     });
}

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNpeForNullOntologyId() {
    assertThrows(NullPointerException.class, () -> { 
        impl.getSignature(CLASS, null);
     });
}
}
