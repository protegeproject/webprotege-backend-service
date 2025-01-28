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
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntologyID;

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
public class InverseObjectPropertyAxiomsIndexImpl_TestCase {

    private InverseObjectPropertyAxiomsIndexImpl impl;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLObjectProperty property;

    @Mock
    private OWLInverseObjectPropertiesAxiom axiom;

    @Mock
    private AxiomsByTypeIndex axiomsByTypeIndex;

    @BeforeEach
    public void setUp() {
        when(axiom.getFirstProperty())
                .thenReturn(property);
        when(axiom.getSecondProperty())
                .thenReturn(property);
        when(axiomsByTypeIndex.getAxiomsByType(any(), any()))
                .thenAnswer(invocation -> Stream.of());
        when(axiomsByTypeIndex.getAxiomsByType(AxiomType.INVERSE_OBJECT_PROPERTIES, ontologyId))
                .thenAnswer(invocation -> Stream.of(axiom));
        impl = new InverseObjectPropertyAxiomsIndexImpl(axiomsByTypeIndex);
    }

    @Test
    public void shouldGetDependencies() {
        assertThat(impl.getDependencies(), contains(axiomsByTypeIndex));
    }

    @Test
    public void shouldGetInverseObjectPropertiesAxiomForProperty() {
        var axioms = impl.getInverseObjectPropertyAxioms(property, ontologyId).collect(toSet());
        assertThat(axioms, hasItem(axiom));
    }

    @Test
    public void shouldGetEmptySetForUnknownOntologyId() {
        var axioms = impl.getInverseObjectPropertyAxioms(property, mock(OWLOntologyID.class)).collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }

    @Test
    public void shouldGetEmptySetForUnknownClass() {
        var axioms = impl.getInverseObjectPropertyAxioms(mock(OWLObjectProperty.class), ontologyId).collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNpeForNullOntologyId() {
    assertThrows(NullPointerException.class, () -> { 
        impl.getInverseObjectPropertyAxioms(property, null);
     });
}

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNpeForNullProperty() {
    assertThrows(NullPointerException.class, () -> { 
        impl.getInverseObjectPropertyAxioms(null, ontologyId);
     });
}


}
