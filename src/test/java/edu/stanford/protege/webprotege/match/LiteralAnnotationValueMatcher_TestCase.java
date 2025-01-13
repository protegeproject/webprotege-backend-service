package edu.stanford.protege.webprotege.match;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnonymousIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Jun 2018
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class LiteralAnnotationValueMatcher_TestCase {

    private LiteralAnnotationValueMatcher matcher;

    @Mock
    private Matcher<OWLLiteral> literalMatcher;

    @Mock
    private OWLLiteral literal;

    @Mock
    private IRI iri;

    @Mock
    private OWLAnonymousIndividual individual;

    @BeforeEach
    public void setUp() throws Exception {
        matcher = new LiteralAnnotationValueMatcher(literalMatcher);

        when(literalMatcher.matches(literal)).thenReturn(true);

    }

    @Test
    public void shouldMatchLiteral() {
        assertThat(matcher.matches(literal), is(true));
    }

    @Test
    public void shouldNotMatchLiteral() {
        when(literalMatcher.matches(literal)).thenReturn(false);
        assertThat(matcher.matches(literal), is(false));
    }

    @Test
    public void shouldNotMatchIri() {
        assertThat(matcher.matches(iri), is(false));
    }

    @Test
    public void shouldNotMatchAnonymousIndividual() {
        assertThat(matcher.matches(individual), is(false));
    }
}
