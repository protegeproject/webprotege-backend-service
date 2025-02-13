package edu.stanford.protege.webprotege.axiom;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.OWLAxiom;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/02/15
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AxiomComparatorImpl_TestCase {

    public static final int BY_SUBJECT_DIFF = -5;
    public static final int TYPE_DIFF = -7;
    public static final int RENDERING_DIFF = -10;
    public static final int COMPARE_SAME = 0;

    @Mock
    private AxiomBySubjectComparator bySubjectComparator;

    @Mock
    private AxiomByTypeComparator byTypeComparator;

    @Mock
    private AxiomByRenderingComparator byRenderingComparator;

    @Mock
    private OWLAxiom axiom1, axiom2;

    private AxiomComparatorImpl comparator;

    @BeforeEach
    public void setUp() throws Exception {
        comparator = new AxiomComparatorImpl(
                bySubjectComparator,
                byTypeComparator,
                byRenderingComparator);
    }

    @Test
    public void shouldCompareBySubjectFirst() {
        when(bySubjectComparator.compare(axiom1, axiom2)).thenReturn(BY_SUBJECT_DIFF);
        assertThat(comparator.compare(axiom1, axiom2), is(BY_SUBJECT_DIFF));
        verify(byTypeComparator, never()).compare(axiom1, axiom2);
        verify(byRenderingComparator, never()).compare(axiom1, axiom2);
    }

    @Test
    public void shouldCompareByTypeSecond() {
        // Same subject.  Now compare by type
        when(bySubjectComparator.compare(axiom1, axiom2)).thenReturn(COMPARE_SAME);
        when(byTypeComparator.compare(axiom1, axiom2)).thenReturn(TYPE_DIFF);
        assertThat(comparator.compare(axiom1, axiom2), is(TYPE_DIFF));
        verify(byRenderingComparator, never()).compare(axiom1, axiom2);
    }

    @Test
    public void shouldCompareByRenderingThird() {
        // Same subject.  Same type.  Not compare by rendering.
        when(bySubjectComparator.compare(axiom1, axiom2)).thenReturn(COMPARE_SAME);
        when(byTypeComparator.compare(axiom1, axiom2)).thenReturn(COMPARE_SAME);
        when(byRenderingComparator.compare(axiom1, axiom2)).thenReturn(RENDERING_DIFF);
        assertThat(comparator.compare(axiom1, axiom2), is(RENDERING_DIFF));
    }
}
