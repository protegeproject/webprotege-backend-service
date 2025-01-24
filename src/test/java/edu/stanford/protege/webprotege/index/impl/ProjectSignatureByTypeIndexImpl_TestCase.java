package edu.stanford.protege.webprotege.index.impl;

import edu.stanford.protege.webprotege.index.OntologySignatureByTypeIndex;
import edu.stanford.protege.webprotege.index.ProjectOntologiesIndex;
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
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-16
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ProjectSignatureByTypeIndexImpl_TestCase {

    private ProjectSignatureByTypeIndexImpl impl;


    @Mock
    private ProjectOntologiesIndex projectOntologiesIndex;

    @Mock
    private OntologySignatureByTypeIndex ontologySignatureByTypeIndex;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLClass cls;

    @Mock
    private OWLDatatype datatype;

    @Mock
    private OWLObjectProperty objectProperty;

    @Mock
    private OWLDataProperty dataProperty;

    @Mock
    private OWLAnnotationProperty annotationProperty;

    @Mock
    private OWLNamedIndividual individual;

    @Mock
    private AxiomsByEntityReferenceIndexImpl axiomsByEntityReferenceImpl;

    @BeforeEach
    public void setUp() {
        when(axiomsByEntityReferenceImpl.getProjectAxiomsSignature(EntityType.CLASS))
                .thenAnswer(invocation -> Stream.of(cls));
        when(axiomsByEntityReferenceImpl.getProjectAxiomsSignature(EntityType.OBJECT_PROPERTY))
                .thenAnswer(invocation -> Stream.of(objectProperty));
        when(axiomsByEntityReferenceImpl.getProjectAxiomsSignature(EntityType.DATA_PROPERTY))
                .thenAnswer(invocation -> Stream.of(dataProperty));
        when(axiomsByEntityReferenceImpl.getProjectAxiomsSignature(EntityType.ANNOTATION_PROPERTY))
                .thenAnswer(invocation -> Stream.of(annotationProperty));
        when(axiomsByEntityReferenceImpl.getProjectAxiomsSignature(EntityType.DATATYPE))
                .thenAnswer(invocation -> Stream.of(datatype));
        when(axiomsByEntityReferenceImpl.getProjectAxiomsSignature(EntityType.NAMED_INDIVIDUAL))
                .thenAnswer(invocation -> Stream.of(individual));
        impl = new ProjectSignatureByTypeIndexImpl(axiomsByEntityReferenceImpl);
    }

    @Test
    public void shouldGetDependencies() {
        assertThat(impl.getDependencies(), contains(axiomsByEntityReferenceImpl));
    }

    @Test
    public void shouldGetClassesInSignature() {
        var signature = impl.getSignature(EntityType.CLASS).collect(toSet());
        assertThat(signature, hasItem(cls));
    }

    @Test
    public void shouldGetDatatypesInSignature() {
        var signature = impl.getSignature(EntityType.DATATYPE).collect(toSet());
        assertThat(signature, hasItem(datatype));
    }

    @Test
    public void shouldGetObjectPropertiesInSignature() {
        var signature = impl.getSignature(EntityType.OBJECT_PROPERTY).collect(toSet());
        assertThat(signature, hasItem(objectProperty));
    }

    @Test
    public void shouldGetDataPropertiesInSignature() {
        var signature = impl.getSignature(EntityType.DATA_PROPERTY).collect(toSet());
        assertThat(signature, hasItem(dataProperty));
    }

    @Test
    public void shouldGetAnnotationPropertiesInSignature() {
        var signature = impl.getSignature(EntityType.ANNOTATION_PROPERTY).collect(toSet());
        assertThat(signature, hasItem(annotationProperty));
    }

    @Test
    public void shouldGetIndividualsPropertiesInSignature() {
        var signature = impl.getSignature(EntityType.NAMED_INDIVIDUAL).collect(toSet());
        assertThat(signature, hasItem(individual));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNpeIfOntologyIdIsNull() {
    assertThrows(NullPointerException.class, () -> { 
        impl.getSignature(null);
     });
}
}
