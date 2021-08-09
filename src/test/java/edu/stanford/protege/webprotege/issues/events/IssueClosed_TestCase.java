
package edu.stanford.protege.webprotege.issues.events;

import edu.stanford.protege.webprotege.common.UserId;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class IssueClosed_TestCase {

    private IssueClosed issueClosed;

    @Mock
    private UserId userId;

    private long timestamp = 1L;

    @Before
    public void setUp() {
        issueClosed = new IssueClosed(userId, timestamp);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_userId_IsNull() {
        new IssueClosed(null, timestamp);
    }

    @Test
    public void shouldBeEqualToSelf() {
        MatcherAssert.assertThat(issueClosed, Matchers.is(issueClosed));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        MatcherAssert.assertThat(issueClosed.equals(null), Matchers.is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        MatcherAssert.assertThat(issueClosed, Matchers.is(new IssueClosed(userId, timestamp)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_userId() {
        MatcherAssert.assertThat(issueClosed, Matchers.is(Matchers.not(new IssueClosed(Mockito.mock(UserId.class), timestamp))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_timestamp() {
        MatcherAssert.assertThat(issueClosed, Matchers.is(Matchers.not(new IssueClosed(userId, 2L))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        MatcherAssert.assertThat(issueClosed.hashCode(), Matchers.is(new IssueClosed(userId, timestamp).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        MatcherAssert.assertThat(issueClosed.toString(), Matchers.startsWith("IssueClosed"));
    }

}
