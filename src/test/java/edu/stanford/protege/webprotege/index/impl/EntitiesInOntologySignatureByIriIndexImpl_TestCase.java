package edu.stanford.protege.webprotege.index.impl;

import edu.stanford.protege.webprotege.index.OntologyAnnotationsSignatureIndex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-09-18
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class EntitiesInOntologySignatureByIriIndexImpl_TestCase {

    private EntitiesInOntologySignatureByIriIndexImpl impl;

    @Mock
    private AxiomsByEntityReferenceIndexImpl axiomByEntityReference;

    @Mock
    private IRI iri;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLEntity entity;

    @Mock
    private OntologyAnnotationsSignatureIndex ontologyAnnotationsSignatureIndex;

    @Mock
    private OWLAnnotationProperty otherEntity;

    @Mock
    private IRI otherIri;

    @BeforeEach
    public void setUp() {
        impl = new EntitiesInOntologySignatureByIriIndexImpl(axiomByEntityReference, ontologyAnnotationsSignatureIndex);
        when(axiomByEntityReference.getEntitiesInSignatureWithIri(any(), any()))
                .thenAnswer(inv -> Stream.empty());
        when(axiomByEntityReference.getEntitiesInSignatureWithIri(iri, ontologyId))
                .thenAnswer(inv -> Stream.of(entity));
        when(otherEntity.getIRI())
                .thenReturn(otherIri);
        when(ontologyAnnotationsSignatureIndex.getOntologyAnnotationsSignature(any()))
                .thenAnswer(inv -> Stream.empty());
        when(ontologyAnnotationsSignatureIndex.getOntologyAnnotationsSignature(ontologyId))
                .thenAnswer(inv -> Stream.of(otherEntity));
    }

    @Test
    public void shouldGetEntitiesInAxiomSignature() {
        var sig = impl.getEntitiesInSignature(iri, ontologyId).collect(toSet());
        assertThat(sig, contains(entity));
    }

    @Test
    public void shouldGetEntitiesInOntologyAnnotationsSignature() {
        var sig = impl.getEntitiesInSignature(otherIri, ontologyId).collect(toSet());
        assertThat(sig, contains(otherEntity));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNpeForNullIri() {
    assertThrows(NullPointerException.class, () -> { 
        impl.getEntitiesInSignature(null, ontologyId);
     });
}

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNpeForNullOntologyId() {
    assertThrows(NullPointerException.class, () -> { 
        impl.getEntitiesInSignature(iri, null);
     });
}

    @Test
    public void shouldGetDependencies() {
        assertThat(impl.getDependencies(), contains(axiomByEntityReference));
    }
}
