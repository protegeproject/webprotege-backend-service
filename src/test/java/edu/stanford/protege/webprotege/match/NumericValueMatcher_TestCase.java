package edu.stanford.protege.webprotege.match;

import edu.stanford.protege.webprotege.criteria.NumericPredicate;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Jun 2018
 */
public class NumericValueMatcher_TestCase {

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void shouldMatchLessThan() {
        NumericValueMatcher matcher = new NumericValueMatcher(NumericPredicate.LESS_THAN, 10);
        assertThat(matcher.matches("9"), Matchers.is(true));
        assertThat(matcher.matches("10"), Matchers.is(false));
        assertThat(matcher.matches("11"), Matchers.is(false));
    }

    @Test
    public void shouldMatchLessThanOrEqualTo() {
        NumericValueMatcher matcher = new NumericValueMatcher(NumericPredicate.LESS_THAN_OR_EQUAL_TO, 10);
        assertThat(matcher.matches("9"), Matchers.is(true));
        assertThat(matcher.matches("10"), Matchers.is(true));
        assertThat(matcher.matches("11"), Matchers.is(false));
    }

    @Test
    public void shouldMatchGreaterThan() {
        NumericValueMatcher matcher = new NumericValueMatcher(NumericPredicate.GREATER_THAN, 10);
        assertThat(matcher.matches("9"), Matchers.is(false));
        assertThat(matcher.matches("10"), Matchers.is(false));
        assertThat(matcher.matches("11"), Matchers.is(true));
    }

    @Test
    public void shouldMatchGreaterThanOrEqualTo() {
        NumericValueMatcher matcher = new NumericValueMatcher(NumericPredicate.GREATER_THAN_OR_EQUAL_TO, 10);
        assertThat(matcher.matches("9"), Matchers.is(false));
        assertThat(matcher.matches("10"), Matchers.is(true));
        assertThat(matcher.matches("11"), Matchers.is(true));
    }

    @Test
    public void shouldMatchEqualTo() {
        NumericValueMatcher matcher = new NumericValueMatcher(NumericPredicate.IS_EQUAL_TO, 10);
        assertThat(matcher.matches("9"), Matchers.is(false));
        assertThat(matcher.matches("10"), Matchers.is(true));
        assertThat(matcher.matches("11"), Matchers.is(false));
    }
}
