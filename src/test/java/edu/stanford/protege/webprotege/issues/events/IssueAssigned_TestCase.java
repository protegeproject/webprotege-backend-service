
package edu.stanford.protege.webprotege.issues.events;

import edu.stanford.protege.webprotege.MockingUtils;
import edu.stanford.protege.webprotege.common.UserId;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class IssueAssigned_TestCase {

    private IssueAssigned issueAssigned;

    private UserId userId = edu.stanford.protege.webprotege.MockingUtils.mockUserId();

    private long timestamp = 1L;

    private UserId assignee = MockingUtils.mockUserId();

    @Before
    public void setUp() {
        issueAssigned = new IssueAssigned(userId, timestamp, assignee);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_userId_IsNull() {
        new IssueAssigned(null, timestamp, assignee);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_assignee_IsNull() {
        new IssueAssigned(userId, timestamp, null);
    }

    @Test
    public void shouldReturnSupplied_assignee() {
        MatcherAssert.assertThat(issueAssigned.getAssignee(), Matchers.is(this.assignee));
    }

    @Test
    public void shouldBeEqualToSelf() {
        MatcherAssert.assertThat(issueAssigned, Matchers.is(issueAssigned));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        MatcherAssert.assertThat(issueAssigned.equals(null), Matchers.is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        MatcherAssert.assertThat(issueAssigned, Matchers.is(new IssueAssigned(userId, timestamp, assignee)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_userId() {
        MatcherAssert.assertThat(issueAssigned, Matchers.is(Matchers.not(new IssueAssigned(edu.stanford.protege.webprotege.MockingUtils.mockUserId(), timestamp, assignee))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_timestamp() {
        MatcherAssert.assertThat(issueAssigned, Matchers.is(Matchers.not(new IssueAssigned(userId, 2L, assignee))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_assignee() {
        MatcherAssert.assertThat(issueAssigned, Matchers.is(Matchers.not(new IssueAssigned(userId, timestamp, edu.stanford.protege.webprotege.MockingUtils.mockUserId()))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        MatcherAssert.assertThat(issueAssigned.hashCode(), Matchers.is(new IssueAssigned(userId, timestamp, assignee).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        MatcherAssert.assertThat(issueAssigned.toString(), Matchers.startsWith("IssueAssigned"));
    }

}
