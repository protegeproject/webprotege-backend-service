package edu.stanford.protege.webprotege.match;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

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
public class OrMatcher_TestCase<T> {

    private OrMatcher<T> matcher;

    @Mock
    private Matcher<T> matcherA, matcherB;

    @Mock
    private T value;

    private ImmutableList<Matcher<T>> matchers;

    @BeforeEach
    public void setUp() {
        matchers = ImmutableList.of(matcherA, matcherB);
        matcher = new OrMatcher<>(matchers);
    }

    @Test
    public void shouldNotMatchValue() {
        assertThat(matcher.matches(value), is(false));
    }

    @Test
    public void shouldMatchAtLeastOne() {
        when(matcherA.matches(value)).thenReturn(true);
        assertThat(matcher.matches(value), is(true));
    }

    @Test
    public void shouldMatchAll() {
        when(matcherA.matches(value)).thenReturn(true);
        assertThat(matcher.matches(value), is(true));
    }
}
