package edu.stanford.protege.webprotege.match;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Jun 2018
 */
public class StringContainsRegexMatchMatcher_TestCase {

    private StringContainsRegexMatchMatcher matcher;

    @BeforeEach
    public void setUp() {
        matcher = new StringContainsRegexMatchMatcher(Pattern.compile("[A-Z]"));
    }

    @Test
    public void shouldMatchValue() {
        assertThat(matcher.matches("A"), is(true));
    }

    @Test
    public void shouldNotMatchValue() {
        assertThat(matcher.matches("0"), is(false));
    }
}
