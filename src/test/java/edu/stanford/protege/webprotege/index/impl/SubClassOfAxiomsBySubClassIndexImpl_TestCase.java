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
import org.semanticweb.owlapi.model.*;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Class;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.SubClassOf;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-09
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class SubClassOfAxiomsBySubClassIndexImpl_TestCase {

    private SubClassOfAxiomsBySubClassIndexImpl impl;

    @Mock
    private OWLOntologyID ontologyID;

    private OWLClass cls;

    private OWLSubClassOfAxiom axiom;

    @Mock
    private OWLClassExpression superCls;

    @BeforeEach
    public void setUp() {
        cls = Class(mock(IRI.class));
        axiom = SubClassOf(cls, superCls);
        impl = new SubClassOfAxiomsBySubClassIndexImpl();
        impl.applyChanges(ImmutableList.of(AddAxiomChange.of(ontologyID, axiom)));
    }

    @Test
    public void shouldGetSubClassOfAxiomForSubClass() {
        var axioms = impl.getSubClassOfAxiomsForSubClass(cls, ontologyID).collect(toSet());
        assertThat(axioms, hasItem(axiom));
    }

    @Test
    public void shouldGetEmptySetForUnknownOntologyId() {
        var axioms = impl.getSubClassOfAxiomsForSubClass(cls, mock(OWLOntologyID.class)).collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }

    @Test
    public void shouldGetEmptySetForUnknownClass() {
        var axioms = impl.getSubClassOfAxiomsForSubClass(mock(OWLClass.class), ontologyID).collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNpeForNullOntologyId() {
    assertThrows(NullPointerException.class, () -> { 
        impl.getSubClassOfAxiomsForSubClass(cls, null);
     });
}

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNpeForNullCls() {
    assertThrows(NullPointerException.class, () -> { 
        impl.getSubClassOfAxiomsForSubClass(null, ontologyID);
     });
}
}
