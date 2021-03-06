package edu.stanford.protege.webprotege.index.impl;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.change.AddAxiomChange;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
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
@RunWith(MockitoJUnitRunner.class)
public class ClassAssertionAxiomsByClassIndexImpl_TestCase {

    private ClassAssertionAxiomsByClassIndexImpl impl;

    @Mock
    private OWLOntologyID ontologyID;

    @Mock
    private OWLClass cls;

    @Mock
    private OWLClassAssertionAxiom axiom;

    @Before
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
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeForNullOntologyId() {
        impl.getClassAssertionAxioms(cls, null);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeForNullClass() {
        impl.getClassAssertionAxioms(null, ontologyID);
    }

}
