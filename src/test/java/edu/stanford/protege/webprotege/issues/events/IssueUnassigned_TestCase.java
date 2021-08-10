
package edu.stanford.protege.webprotege.issues.events;

import edu.stanford.protege.webprotege.MockingUtils;
import edu.stanford.protege.webprotege.common.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class IssueUnassigned_TestCase {

    private IssueUnassigned issueUnassigned;

    private UserId userId = edu.stanford.protege.webprotege.MockingUtils.mockUserId();

    private long timestamp = 1L;

    private UserId assignee = MockingUtils.mockUserId();

    @Before
    public void setUp() {
        issueUnassigned = new IssueUnassigned(userId, timestamp, assignee);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_userId_IsNull() {
        new IssueUnassigned(null, timestamp, assignee);
    }

    @Test
    public void shouldReturnSupplied_userId() {
        assertThat(issueUnassigned.getUserId(), is(this.userId));
    }

    @Test
    public void shouldReturnSupplied_timestamp() {
        assertThat(issueUnassigned.getTimestamp(), is(this.timestamp));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_assignee_IsNull() {
        new IssueUnassigned(userId, timestamp, null);
    }

    @Test
    public void shouldReturnSupplied_assignee() {
        assertThat(issueUnassigned.getAssignee(), is(this.assignee));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(issueUnassigned, is(issueUnassigned));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(issueUnassigned.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(issueUnassigned, is(new IssueUnassigned(userId, timestamp, assignee)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_userId() {
        assertThat(issueUnassigned, is(not(new IssueUnassigned(edu.stanford.protege.webprotege.MockingUtils.mockUserId(), timestamp, assignee))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_timestamp() {
        assertThat(issueUnassigned, is(not(new IssueUnassigned(userId, 2L, assignee))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_assignee() {
        assertThat(issueUnassigned, is(not(new IssueUnassigned(userId, timestamp, edu.stanford.protege.webprotege.MockingUtils.mockUserId()))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(issueUnassigned.hashCode(), is(new IssueUnassigned(userId, timestamp, assignee).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(issueUnassigned.toString(), startsWith("IssueUnassigned"));
    }
}
