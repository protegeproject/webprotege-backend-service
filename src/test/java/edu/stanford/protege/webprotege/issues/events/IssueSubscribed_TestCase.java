
package edu.stanford.protege.webprotege.issues.events;

import edu.stanford.protege.webprotege.MockingUtils;
import edu.stanford.protege.webprotege.common.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(MockitoJUnitRunner.class)
public class IssueSubscribed_TestCase {

    private IssueSubscribed issueSubscribed;

    private UserId userId = edu.stanford.protege.webprotege.MockingUtils.mockUserId();

    private long timestamp = 1L;

    private UserId subscriber = MockingUtils.mockUserId();

    @Before
    public void setUp() {
        issueSubscribed = new IssueSubscribed(userId, timestamp, subscriber);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_userId_IsNull() {
        new IssueSubscribed(null, timestamp, subscriber);
    }

    @Test
    public void shouldReturnSupplied_userId() {
        assertThat(issueSubscribed.getUserId(), is(this.userId));
    }

    @Test
    public void shouldReturnSupplied_timestamp() {
        assertThat(issueSubscribed.getTimestamp(), is(this.timestamp));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_subscriber_IsNull() {
        new IssueSubscribed(userId, timestamp, null);
    }

    @Test
    public void shouldReturnSupplied_subscriber() {
        assertThat(issueSubscribed.getSubscriber(), is(this.subscriber));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(issueSubscribed, is(issueSubscribed));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(issueSubscribed.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(issueSubscribed, is(new IssueSubscribed(userId, timestamp, subscriber)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_userId() {
        assertThat(issueSubscribed, is(not(new IssueSubscribed(edu.stanford.protege.webprotege.MockingUtils.mockUserId(), timestamp, subscriber))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_timestamp() {
        assertThat(issueSubscribed, is(not(new IssueSubscribed(userId, 2L, subscriber))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_subscriber() {
        assertThat(issueSubscribed, is(not(new IssueSubscribed(userId, timestamp, edu.stanford.protege.webprotege.MockingUtils.mockUserId()))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(issueSubscribed.hashCode(), is(new IssueSubscribed(userId, timestamp, subscriber).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(issueSubscribed.toString(), startsWith("IssueSubscribed"));
    }

}
