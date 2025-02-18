package edu.stanford.protege.webprotege.match;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.IRI;

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
public class IriLexicalValueMatcher_TestCase {

    public IriLexicalValueMatcher matcher;

    @Mock
    private Matcher<String> lexicalValueMatcher;

    @Mock
    private IRI iri;

    private String lexicalValue = "The Lexical Value";

    @BeforeEach
    public void setUp() {
        matcher = new IriLexicalValueMatcher(lexicalValueMatcher);

        when(iri.toString()).thenReturn(lexicalValue);
    }

    @Test
    public void shouldMatchLexicalValue() {
        when(lexicalValueMatcher.matches(lexicalValue)).thenReturn(true);
        assertThat(matcher.matches(iri), is(true));
    }
}
