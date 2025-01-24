package edu.stanford.protege.webprotege.index.impl;

import edu.stanford.protege.webprotege.index.AnnotationAxiomsByIriReferenceIndex;
import edu.stanford.protege.webprotege.index.AxiomsByEntityReferenceIndex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.*;

import java.util.Collections;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-06
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AxiomsByReferenceIndexImpl_TestCase {

    private AxiomsByReferenceIndexImpl impl;

    @Mock
    private AxiomsByEntityReferenceIndex axiomsByEntityReferenceIndex;

    @Mock
    private AnnotationAxiomsByIriReferenceIndex axiomsByIriReferenceIndex;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLEntity entity;

    @Mock
    private IRI entityIri;

    @Mock
    private OWLAxiom entityRefAxiom;

    @Mock
    private OWLAnnotationAxiom iriRefAxiom;

    @BeforeEach
    public void setUp() {
        when(entity.getIRI()).thenReturn(entityIri);
        when(axiomsByEntityReferenceIndex.getReferencingAxioms(any(), any())).thenReturn(Stream.empty());
        when(axiomsByEntityReferenceIndex.getReferencingAxioms(entity, ontologyId)).thenReturn(Stream.of(entityRefAxiom));
        when(axiomsByIriReferenceIndex.getReferencingAxioms(any(), any())).thenReturn(Stream.empty());
        when(axiomsByIriReferenceIndex.getReferencingAxioms(entityIri, ontologyId)).thenReturn(Stream.of(iriRefAxiom));
        impl = new AxiomsByReferenceIndexImpl(axiomsByEntityReferenceIndex, axiomsByIriReferenceIndex);
    }

    @Test
    public void shouldGetDependencies() {
        assertThat(impl.getDependencies(), containsInAnyOrder(axiomsByEntityReferenceIndex, axiomsByIriReferenceIndex));
    }

    @Test
    public void shouldGetAxiomsByReference() {
        var referencingAxiomsStream = impl.getReferencingAxioms(Collections.singleton(entity), ontologyId);
        var referencingAxioms = referencingAxiomsStream.collect(toSet());
        assertThat(referencingAxioms, hasItems(entityRefAxiom, iriRefAxiom));
    }

    @Test
    public void shouldGetEmptyStreamForUnknownOntologyId() {
        var referencingAxiomsStream = impl.getReferencingAxioms(Collections.singleton(entity), mock(OWLOntologyID.class));
        var axiomsCount = referencingAxiomsStream.count();
        assertThat(axiomsCount, is(0L));
    }

    @Test
    public void shouldGetEmptyStreamForEmptyEntitiesSet() {
        var referencingAxiomsStream = impl.getReferencingAxioms(Collections.emptySet(), ontologyId);
        var axiomsCount = referencingAxiomsStream.count();
        assertThat(axiomsCount, is(0L));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNullPointerExceptionForNullEntitiesSet() {
    assertThrows(NullPointerException.class, () -> { 
        impl.getReferencingAxioms(null, ontologyId);
     });
}

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNullPointerExceptionForNullOntologyId() {
    assertThrows(NullPointerException.class, () -> { 
        impl.getReferencingAxioms(Collections.singleton(entity), null);
     });
}
}
