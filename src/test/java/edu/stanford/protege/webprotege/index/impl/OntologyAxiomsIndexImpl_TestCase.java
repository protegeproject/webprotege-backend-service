package edu.stanford.protege.webprotege.index.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-20
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class OntologyAxiomsIndexImpl_TestCase {

    private OntologyAxiomsIndexImpl impl;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLAxiom axiom;

    @Mock
    private AxiomsByTypeIndexImpl axiomsByTypeIndexImpl;

    @BeforeEach
    public void setUp() {
        impl = new OntologyAxiomsIndexImpl(axiomsByTypeIndexImpl);


        when(axiomsByTypeIndexImpl.getAxiomsByType(any(), any()))
                .thenAnswer(invocation -> Stream.empty());
        when(axiomsByTypeIndexImpl.getAxiomsByType(AxiomType.CLASS_ASSERTION, ontologyId))
                .thenAnswer(invocation -> Stream.of(axiom));
    }

    @Test
    public void shouldGetDependencies() {
        assertThat(impl.getDependencies(), contains(axiomsByTypeIndexImpl));
    }

    @Test
    public void shouldGetAxioms() {
        var axioms = impl.getAxioms(ontologyId).collect(toSet());
        assertThat(axioms, contains(axiom));
    }

    @Test
    public void shouldGetEmptyStreamForUnknownOntologyId() {
        var axioms = impl.getAxioms(mock(OWLOntologyID.class)).collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNpeIfOntologyIdIsNull() {
    assertThrows(NullPointerException.class, () -> { 
        impl.getAxioms(null);
     });
}

    @Test
    public void shouldContainAxiom() {
        when(axiomsByTypeIndexImpl.containsAxiom(axiom, ontologyId))
                .thenReturn(true);
        assertThat(impl.containsAxiom(axiom, ontologyId), is(true));
    }

    @Test
    public void shouldContainAxiomWithoutAnnotationsIgnoreAnnotations() {
        when(axiomsByTypeIndexImpl.containsAxiom(axiom, ontologyId))
                .thenReturn(true);
        assertThat(impl.containsAxiomIgnoreAnnotations(axiom, ontologyId), is(true));
    }

    @Test
    public void shouldContainAxiomIgnoreAnnotations() {
        when(axiomsByTypeIndexImpl.containsAxiom(axiom, ontologyId))
                .thenReturn(false);
        when(axiomsByTypeIndexImpl.containsAxiom(axiom, ontologyId))
                .thenReturn(true);
        assertThat(impl.containsAxiomIgnoreAnnotations(axiom, ontologyId), is(true));
    }

    @Test
    public void shouldNotContainAxiomInUnknownOntology() {
        assertThat(impl.containsAxiom(axiom, mock(OWLOntologyID.class)), is(false));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNpeIfOntologyIsNullInContainsAxiom() {
    assertThrows(NullPointerException.class, () -> { 
        impl.containsAxiom(axiom, null);
     });
}

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNpeIfAxiomIsNullInContainsAxiom() {
    assertThrows(NullPointerException.class, () -> { 
        impl.containsAxiom(null, ontologyId);
     });
}

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNpeIfOntologyIsNullInContainsAxiomIgnoreAnnotations() {
    assertThrows(NullPointerException.class, () -> { 
        impl.containsAxiomIgnoreAnnotations(axiom, null);
     });
}

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNpeIfAxiomIsNullInContainsAxiomIgnoreAnnotations() {
    assertThrows(NullPointerException.class, () -> { 
        impl.containsAxiomIgnoreAnnotations(null, ontologyId);
     });
}
}
