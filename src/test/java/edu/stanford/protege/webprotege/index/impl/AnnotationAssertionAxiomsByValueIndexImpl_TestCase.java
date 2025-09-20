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
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AnnotationAssertionAxiomsByValueIndexImpl_TestCase {

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLAnnotationAssertionAxiom axiom;

    @Mock
    private IRI iriValue;

    @Mock
    private OWLAnonymousIndividual individualValue;

    @Mock
    private OWLLiteral literalValue;

    private AnnotationAssertionAxiomsByValueIndexImpl index;

    @BeforeEach
    public void setUp() throws Exception {
        index = new AnnotationAssertionAxiomsByValueIndexImpl();
    }


    @Test
    public void shouldIndexAxiomByIriValue() {
        when(axiom.getValue()).thenReturn(iriValue);
        index.applyChanges(ImmutableList.of(AddAxiomChange.of(ontologyId, axiom)));
        assertThat(index.getAxiomsByValue(iriValue, ontologyId).collect(toSet()), contains(axiom));
    }

    @Test
    public void shouldIndexAxiomByAnonymousIndividualValue() {
        when(axiom.getValue()).thenReturn(individualValue);
        index.applyChanges(ImmutableList.of(AddAxiomChange.of(ontologyId, axiom)));
        assertThat(index.getAxiomsByValue(individualValue, ontologyId).collect(toSet()), contains(axiom));
    }

    @Test
    public void shouldNotIndexAxiomByLiteralValue() {
        when(axiom.getValue()).thenReturn(literalValue);
        index.applyChanges(ImmutableList.of(AddAxiomChange.of(ontologyId, axiom)));
        assertThat(index.getAxiomsByValue(literalValue, ontologyId).collect(toSet()), not(contains(axiom)));
    }

    /**
     * @noinspection ConstantConditions
     */
    @Test
    public void shouldThrowNpeIfAxiomIsNull() {
        assertThrows(NullPointerException.class, () -> {
            index.getAxiomsByValue(null, ontologyId);
        });
    }

    /**
     * @noinspection ConstantConditions
     */
    @Test
    public void shouldThrowNpeIfOntologyIdIsNull() {
        assertThrows(NullPointerException.class, () -> {
            index.getAxiomsByValue(iriValue, null);
        });
    }

    @Test
    public void shouldResetIndex() {
        when(axiom.getValue()).thenReturn(iriValue);
        index.applyChanges(ImmutableList.of(AddAxiomChange.of(ontologyId, axiom)));
        assertThat(index.getAxiomsByValue(iriValue, ontologyId).collect(toSet()), contains(axiom));
        index.reset();
        assertThat(index.getAxiomsByValue(iriValue, ontologyId).collect(toSet()).isEmpty(), is(true));

    }
}