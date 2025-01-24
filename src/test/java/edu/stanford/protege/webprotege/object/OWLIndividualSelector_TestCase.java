package edu.stanford.protege.webprotege.object;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/02/15
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class OWLIndividualSelector_TestCase {

    public static final int BEFORE = -1;

    public static final int AFTER = 1;

    private OWLIndividualSelector selector;

    @Mock
    private Comparator<OWLIndividual> individualComparator;

    @Mock
    private OWLNamedIndividual namedIndividual1, namedIndividual2;

    @Mock
    private OWLIndividual individual1, individual2;

    @BeforeEach
    public void setUp() throws Exception {
        selector = new OWLIndividualSelector(individualComparator);
    }


    @Test
    public void shouldNotSelectAnythingForEmptyList() {
        assertThat(selector.selectOne(Collections.emptyList()),
                is(Optional.empty()));
    }

    @Test
    public void shouldSelectAbsentForNoPropertyName() {
        List<OWLIndividual> input = Arrays.asList(individual1, individual2);
        assertThat(selector.selectOne(input),
                is(Optional.empty()));
    }

    @Test
    public void shouldSelectSingleOWLNamedIndividual() {
        List<OWLIndividual> input = Arrays.asList(individual1, individual2, namedIndividual2);
        assertThat(selector.selectOne(input),
                is(Optional.empty()));
    }

    @Test
    public void shouldSelectSmallerOWLObjectProperty() {
        List<OWLIndividual> input = Arrays.asList(individual2, namedIndividual2, namedIndividual1);
        assertThat(selector.selectOne(input),
                is(Optional.empty()));
    }
}
