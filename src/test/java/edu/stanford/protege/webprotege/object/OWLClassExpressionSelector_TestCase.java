package edu.stanford.protege.webprotege.object;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;

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
public class OWLClassExpressionSelector_TestCase {

    public static final int BEFORE = -1;

    public static final int AFTER = 1;

    private OWLClassExpressionSelector selector;

    @Mock
    private Comparator<OWLClassExpression> classComparator;

    @Mock
    private OWLClass cls1, cls2;

    @Mock
    private OWLClassExpression clsExpression1, clsExpression2;

    @BeforeEach
    public void setUp() throws Exception {
        selector = new OWLClassExpressionSelector(classComparator);
    }


    @Test
    public void shouldNotSelectAnythingForEmptyList() {
        assertThat(selector.selectOne(Collections.emptyList()),
                is(Optional.empty()));
    }

    @Test
    public void shouldSelectAbsentForNoClassName() {
        List<OWLClassExpression> input = Arrays.asList(clsExpression1, clsExpression2);
        assertThat(selector.selectOne(input),
                is(Optional.empty()));
    }

    @Test
    public void shouldSelectSingleOWLClass() {
        List<OWLClassExpression> input = Arrays.asList(clsExpression1, clsExpression2, cls2);
        assertThat(selector.selectOne(input),
                is(Optional.empty()));
    }

    @Test
    public void shouldSelectSmallerOWLClass() {
        List<OWLClassExpression> input = Arrays.asList(cls2, cls1, clsExpression1);
        assertThat(selector.selectOne(input),
                is(Optional.empty()));
    }
}
