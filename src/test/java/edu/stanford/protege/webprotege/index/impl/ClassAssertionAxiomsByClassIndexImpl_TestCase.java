package edu.stanford.protege.webprotege.index.impl;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.change.AddAxiomChange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.mockito.Mock;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-19
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ClassAssertionAxiomsByClassIndexImpl_TestCase {

    private ClassAssertionAxiomsByClassIndexImpl impl;

    @Mock
    private OWLOntologyID ontologyID;

    @Mock
    private OWLClass cls;

    @Mock
    private OWLClassAssertionAxiom axiom;

    @BeforeEach
    public void setUp() {
        when(axiom.getClassExpression())
                .thenReturn(cls);
        impl = new ClassAssertionAxiomsByClassIndexImpl();
        impl.applyChanges(ImmutableList.of(AddAxiomChange.of(ontologyID, axiom)));
    }

    @Test
    public void shouldGetClassAssertionAxiomForClass() {
        var axioms = impl.getClassAssertionAxioms(cls, ontologyID).collect(toSet());
        assertThat(axioms, hasItem(axiom));
    }

    @Test
    public void shouldGetEmptySetForUnknownOntologyId() {
        var axioms = impl.getClassAssertionAxioms(cls, mock(OWLOntologyID.class)).collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }

    @Test
    public void shouldGetEmptySetForUnknownClass() {
        var axioms = impl.getClassAssertionAxioms(mock(OWLClass.class), ontologyID).collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNpeForNullOntologyId() {
    assertThrows(NullPointerException.class, () -> { 
        impl.getClassAssertionAxioms(cls, null);
     });
}

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNpeForNullClass() {
    assertThrows(NullPointerException.class, () -> { 
        impl.getClassAssertionAxioms(null, ontologyID);
     });
}

}
