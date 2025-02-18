package edu.stanford.protege.webprotege.index.impl;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.change.AddAxiomChange;
import edu.stanford.protege.webprotege.change.RemoveAxiomChange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-07
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AxiomsByTypeIndexImpl_TestCase {

    private AxiomsByTypeIndexImpl impl;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLSubClassOfAxiom axiom, axiom2;

    @SuppressWarnings("unchecked")
    @BeforeEach
    public void setUp() {
        impl = new AxiomsByTypeIndexImpl();
        when(axiom.getAxiomType())
                .thenReturn((AxiomType) AxiomType.SUBCLASS_OF);
        when(axiom2.getAxiomType())
                .thenReturn((AxiomType) AxiomType.SUBCLASS_OF);
        impl.applyChanges(ImmutableList.of(AddAxiomChange.of(ontologyId, axiom)));
    }

    @Test
    public void shouldGetSubClassOfAxiom() {
        var axiomsStream = impl.getAxiomsByType(AxiomType.SUBCLASS_OF, ontologyId);
        var axioms = axiomsStream.collect(toSet());
        assertThat(axioms, hasItem(axiom));
    }

    @Test
    public void shouldContainAxiom() {
        var containsAxioms = impl.containsAxiom(axiom, ontologyId);
        assertThat(containsAxioms, is(true));
    }

    @Test
    public void shouldNotContainAxiom() {
        var subClassOfAxiom = mock(OWLSubClassOfAxiom.class);
        when(subClassOfAxiom.getAxiomType())
                .thenReturn((AxiomType) AxiomType.SUBCLASS_OF);
        var containsAxiom = impl.containsAxiom(subClassOfAxiom, ontologyId);
        assertThat(containsAxiom, is(false));
    }


    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNpeIfContainsAxiom_Axiom_IsNull() {
    assertThrows(NullPointerException.class, () -> { 
        impl.containsAxiom(null, ontologyId);
     });
}

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNpeIfContainsAxiom_OntologyId_IsNull() {
    assertThrows(NullPointerException.class, () -> { 
        impl.containsAxiom(axiom, null);
     });
}

    @Test
    public void shouldReturnEmptyStreamIfForUnknownOntologyId() {
        var axiomsStream = impl.getAxiomsByType(AxiomType.SUBCLASS_OF, mock(OWLOntologyID.class));
        var axioms = axiomsStream.collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }

    @Test
    public void shouldHandleAddAxiom() {
        impl.applyChanges(ImmutableList.of(AddAxiomChange.of(ontologyId, axiom2)));
        var axioms = impl.getAxiomsByType(AxiomType.SUBCLASS_OF, ontologyId).collect(toSet());
        assertThat(axioms, hasItem(axiom2));
    }

    @Test
    public void shouldHandleRemoveAxiom() {
        impl.applyChanges(ImmutableList.of(RemoveAxiomChange.of(ontologyId, axiom)));
        var axioms = impl.getAxiomsByType(AxiomType.SUBCLASS_OF, ontologyId).collect(toSet());
        assertThat(axioms, not(hasItem(axiom)));
    }

    @Test
public void shouldThrowNpeIfAxiomTypeIsNull() {
    assertThrows(NullPointerException.class, () -> { 
        impl.getAxiomsByType(null, ontologyId);
     });
}

    @Test
public void shouldThrowNpeIfOntologyIdIsNull() {
    assertThrows(NullPointerException.class, () -> { 
        impl.getAxiomsByType(AxiomType.SUBCLASS_OF, null);
     });
}
}
