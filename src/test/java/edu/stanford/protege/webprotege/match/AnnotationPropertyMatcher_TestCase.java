package edu.stanford.protege.webprotege.match;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;

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
public class AnnotationPropertyMatcher_TestCase {

    private AnnotationPropertyMatcher matcher;

    @Mock
    private OWLAnnotationProperty property;

    @Mock
    private IRI iri;

    @Mock
    private Matcher<IRI> iriMatcher;

    @BeforeEach
    public void setUp() {
        matcher = new AnnotationPropertyMatcher(iriMatcher);
        when(property.getIRI()).thenReturn(iri);
    }

    @Test
    public void shouldMatchAnnotationProperty() {
        when(iriMatcher.matches(iri)).thenReturn(true);
        assertThat(matcher.matches(property), is(true));
    }

    @Test
    public void shouldNotMatchAnnotationProperty() {
        when(iriMatcher.matches(iri)).thenReturn(false);
        assertThat(matcher.matches(property), is(false));
    }
}
