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
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLFunctionalDataPropertyAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.stream.Stream;

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
 * 2019-08-10
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class DataPropertyCharacteristicsIndexImpl_TestCase {

    private DataPropertyCharacteristicsIndexImpl impl;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLDataProperty property;

    @Mock
    private AxiomsByTypeIndex axiomsByTypeIndex;

    @Mock
    private OWLFunctionalDataPropertyAxiom axiom;

    @BeforeEach
    public void setUp() {
        when(axiom.getProperty())
                .thenReturn(property);
        when(axiomsByTypeIndex.getAxiomsByType(any(), any()))
                .thenAnswer(invocation -> Stream.empty());
        impl = new DataPropertyCharacteristicsIndexImpl(axiomsByTypeIndex);
    }

    @Test
    public void shouldGetDependencies() {
        assertThat(impl.getDependencies(), contains(axiomsByTypeIndex));
    }

    @Test
    public void shouldReturnTrueForFunctional() {
        when(axiomsByTypeIndex.getAxiomsByType(AxiomType.FUNCTIONAL_DATA_PROPERTY, ontologyId))
                .thenAnswer(invocation -> Stream.of(axiom));
        assertThat(impl.isFunctional(property, ontologyId), is(true));
    }

    @Test
    public void shouldReturnFalseForFunctional() {
        assertThat(impl.isFunctional(property, ontologyId), is(false));
    }

    @Test
    public void shouldReturnFalseForUnknownOntologyId() {
        assertThat(impl.isFunctional(property, mock(OWLOntologyID.class)), is(false));
    }

    @Test
    public void shouldReturnFalseForUnknownProperty() {
        assertThat(impl.isFunctional(mock(OWLDataProperty.class), ontologyId), is(false));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNpeIfPropertyIsNull() {
    assertThrows(NullPointerException.class, () -> { 
        impl.isFunctional(null, ontologyId);
     });
}


    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNpeIfOntologyIdIsNull() {
    assertThrows(NullPointerException.class, () -> { 
        impl.isFunctional(property, null);
     });
}
}
