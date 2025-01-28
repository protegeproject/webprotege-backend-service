package edu.stanford.protege.webprotege.index.impl;

import edu.stanford.protege.webprotege.index.AxiomsByTypeIndex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLDisjointObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntologyID;

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
 * 2019-08-24
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class DisjointObjectPropertiesAxiomsIndexImpl_TestCase {

    private DisjointObjectPropertiesAxiomsIndexImpl impl;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLObjectProperty property;

    @Mock
    private OWLDisjointObjectPropertiesAxiom axiom;

    @Mock
    private AxiomsByTypeIndex axiomsByTypeIndex;

    @BeforeEach
    public void setUp() {
        when(axiom.getProperties())
                .thenReturn(Collections.singleton(property));
        when(axiomsByTypeIndex.getAxiomsByType(any(), any()))
                .thenAnswer(invocation -> Stream.of());
        when(axiomsByTypeIndex.getAxiomsByType(AxiomType.DISJOINT_OBJECT_PROPERTIES, ontologyId))
                .thenAnswer(invocation -> Stream.of(axiom));
        impl = new DisjointObjectPropertiesAxiomsIndexImpl(axiomsByTypeIndex);
    }

    @Test
    public void shouldGetDependencies() {
        assertThat(impl.getDependencies(), contains(axiomsByTypeIndex));
    }

    @Test
    public void shouldGetDisjointObjectPropertiesAxiomForObjectProperty() {
        var axioms = impl.getDisjointObjectPropertiesAxioms(property, ontologyId).collect(toSet());
        assertThat(axioms, hasItem(axiom));
    }

    @Test
    public void shouldGetEmptySetForUnknownOntologyId() {
        var axioms = impl.getDisjointObjectPropertiesAxioms(property, mock(OWLOntologyID.class)).collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }

    @Test
    public void shouldGetEmptySetForUnknownObjectProperty() {
        var axioms = impl.getDisjointObjectPropertiesAxioms(mock(OWLObjectProperty.class), ontologyId).collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNpeForNullOntologyId() {
    assertThrows(NullPointerException.class, () -> { 
        impl.getDisjointObjectPropertiesAxioms(property, null);
     });
}

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNpeForNullObjectProperty() {
    assertThrows(NullPointerException.class, () -> { 
        impl.getDisjointObjectPropertiesAxioms(null, ontologyId);
     });
}
}
