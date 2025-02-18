package edu.stanford.protege.webprotege.match;

import edu.stanford.protege.webprotege.criteria.StringContainsCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class StringContainsMatcher_TestCase {

    public static final boolean DO_NOT_IGNORE_CASE = false;

    public static final boolean IGNORE_CASE = true;

    private StringContainsMatcher matcher;

    @Mock
    private StringContainsCriteria criteria;

    @BeforeEach
    public void setUp() throws Exception {
        matcher = new StringContainsMatcher(criteria);
    }

    @Test
    public void shouldMatch() {
        when(criteria.getValue()).thenReturn("World");
        when(criteria.isIgnoreCase()).thenReturn(DO_NOT_IGNORE_CASE);
        var matches = matcher.matches("Hello World!");
        assertThat(matches, is(true));
    }

    @Test
    public void shouldNotMatch() {
        when(criteria.getValue()).thenReturn("World");
        when(criteria.isIgnoreCase()).thenReturn(DO_NOT_IGNORE_CASE);
        var matches = matcher.matches("Hello world!");
        assertThat(matches, is(false));
    }

    @Test
    public void shouldMatchIgnoringCase() {
        when(criteria.getValue()).thenReturn("World");
        when(criteria.isIgnoreCase()).thenReturn(IGNORE_CASE);
        var matches = matcher.matches("Hello world!");
        assertThat(matches, is(true));
    }
}