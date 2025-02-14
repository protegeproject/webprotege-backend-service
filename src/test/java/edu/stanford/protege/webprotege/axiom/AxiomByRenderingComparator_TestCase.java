package edu.stanford.protege.webprotege.axiom;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.model.OWLAxiom;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/02/15
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AxiomByRenderingComparator_TestCase {

    private AxiomByRenderingComparator renderingComparator;

    @Mock
    private OWLObjectRenderer renderer;

    @Mock
    private OWLAxiom axiom1, axiom2;

    @BeforeEach
    public void setUp() throws Exception {
        renderingComparator = new AxiomByRenderingComparator(renderer);
    }

    @Test
    public void shouldCompareByRendering() {
        when(renderer.render(axiom1)).thenReturn("A");
        when(renderer.render(axiom2)).thenReturn("B");
        assertThat(renderingComparator.compare(axiom1, axiom2), is(lessThan(0)));
    }

    @Test
    public void shouldIgnoreCase() {
        when(renderer.render(axiom1)).thenReturn("a");
        when(renderer.render(axiom2)).thenReturn("A");
        assertThat(renderingComparator.compare(axiom1, axiom2), is(0));
    }
}
