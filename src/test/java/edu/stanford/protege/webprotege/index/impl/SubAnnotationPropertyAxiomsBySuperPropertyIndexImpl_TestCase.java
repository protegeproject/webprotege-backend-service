package edu.stanford.protege.webprotege.index.impl;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.change.AddAxiomChange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLSubAnnotationPropertyOfAxiom;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-17
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class SubAnnotationPropertyAxiomsBySuperPropertyIndexImpl_TestCase {

    private SubAnnotationPropertyAxiomsBySuperPropertyIndexImpl impl;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLSubAnnotationPropertyOfAxiom axiom;

    @Mock
    private OWLAnnotationProperty property;

    @BeforeEach
    public void setUp() {
        when(axiom.getSuperProperty())
                .thenReturn(property);
        impl = new SubAnnotationPropertyAxiomsBySuperPropertyIndexImpl();
        impl.applyChanges(ImmutableList.of(AddAxiomChange.of(ontologyId, axiom)));
    }

    @Test
    public void shouldGetAxiomForSuperProperty() {
        var axioms = impl.getAxiomsForSuperProperty(property, ontologyId).collect(toSet());
        assertThat(axioms, contains(axiom));
    }

    @Test
    public void shouldNotGetAxiomForOtherSuperProperty() {
        var axioms = impl.getAxiomsForSuperProperty(mock(OWLAnnotationProperty.class), ontologyId).collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }

    @Test
    public void shouldNotGetAxiomsForUnknownOntology() {
        var axioms = impl.getAxiomsForSuperProperty(property, mock(OWLOntologyID.class)).collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNpeIfOntologyIsNull() {
    assertThrows(NullPointerException.class, () -> { 
        impl.getAxiomsForSuperProperty(property, null);
     });
}

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNpeIfPropertyIsNull() {
    assertThrows(NullPointerException.class, () -> { 
        impl.getAxiomsForSuperProperty(null, ontologyId);
     });
}
}
