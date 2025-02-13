package edu.stanford.protege.webprotege.axiom;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAxiom;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.doReturn;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 03/02/15
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AxiomByTypeComparator_TestCase {

    private AxiomByTypeComparator comparator;

    @Mock
    private OWLAxiom axiom1, axiom2, axiom3;

    private AxiomType<?>
            firstAxiomType = AxiomType.SUBCLASS_OF,
            secondAxiomType = AxiomType.EQUIVALENT_CLASSES,
            thirdAxiomType = AxiomType.DISJOINT_CLASSES;

    @BeforeEach
    public void setUp() throws Exception {
        List<AxiomType<?>> ordering = new ArrayList<>();
        ordering.add(firstAxiomType);
        ordering.add(secondAxiomType);
        Mockito.doReturn(firstAxiomType).when(axiom1).getAxiomType();
        Mockito.doReturn(secondAxiomType).when(axiom2).getAxiomType();
        comparator = new AxiomByTypeComparator(ordering);
    }

    @Test
    public void shouldReturnMinusOne() {
        assertThat(comparator.compare(axiom1, axiom2), is(-1));
    }

    @Test
    public void shouldReturnPlusOne() {
        assertThat(comparator.compare(axiom2, axiom1), is(1));
    }

    @Test
    public void shouldReturnZero() {
        assertThat(comparator.compare(axiom1, axiom1), is(0));
    }

    @Test
    public void shouldReturnGreaterThanZeroForUnknownType() {
        doReturn(thirdAxiomType).when(axiom3).getAxiomType();
        assertThat(comparator.compare(axiom3, axiom1), is(greaterThan(0)));
    }

    @Test
    public void shouldReturnLessThanZeroForUnknownType() {
        doReturn(thirdAxiomType).when(axiom3).getAxiomType();
        assertThat(comparator.compare(axiom1, axiom3), is(lessThan(0)));
    }
}
