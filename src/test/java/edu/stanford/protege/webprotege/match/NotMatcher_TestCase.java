package edu.stanford.protege.webprotege.match;

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
public class NotMatcher_TestCase<T> {

    private NotMatcher<T> matcher;

    @Mock
    private Matcher<T> innerMatcher;

    @Mock
    private T value;

    @BeforeEach
    public void setUp() {
        matcher = new NotMatcher<>(innerMatcher);
    }

    @Test
    public void shouldMatch() {
        when(innerMatcher.matches(value)).thenReturn(false);
        assertThat(matcher.matches(value), is(true));
    }

    @Test
    public void shouldNotMatch() {
        when(innerMatcher.matches(value)).thenReturn(true);
        assertThat(matcher.matches(value), is(false));
    }
}
