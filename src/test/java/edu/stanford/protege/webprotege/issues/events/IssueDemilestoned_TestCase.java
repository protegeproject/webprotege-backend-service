
package edu.stanford.protege.webprotege.issues.events;

import edu.stanford.protege.webprotege.issues.Milestone;
import edu.stanford.protege.webprotege.user.UserId;
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
public class IssueDemilestoned_TestCase {

    private IssueDemilestoned issueDemilestoned;
    @Mock
    private UserId userId;
    private long timestamp = 1L;
    @Mock
    private Milestone milestone;

    @Before
    public void setUp() {
        issueDemilestoned = new IssueDemilestoned(userId, timestamp, milestone);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_userId_IsNull() {
        new IssueDemilestoned(null, timestamp, milestone);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_milestone_IsNull() {
        new IssueDemilestoned(userId, timestamp, null);
    }

    @Test
    public void shouldReturnSupplied_milestone() {
        MatcherAssert.assertThat(issueDemilestoned.getMilestone(), Matchers.is(this.milestone));
    }

    @Test
    public void shouldBeEqualToSelf() {
        MatcherAssert.assertThat(issueDemilestoned, Matchers.is(issueDemilestoned));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        MatcherAssert.assertThat(issueDemilestoned.equals(null), Matchers.is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        MatcherAssert.assertThat(issueDemilestoned, Matchers.is(new IssueDemilestoned(userId, timestamp, milestone)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_userId() {
        MatcherAssert.assertThat(issueDemilestoned, Matchers.is(Matchers.not(new IssueDemilestoned(Mockito.mock(UserId.class), timestamp, milestone))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_timestamp() {
        MatcherAssert.assertThat(issueDemilestoned, Matchers.is(Matchers.not(new IssueDemilestoned(userId, 2L, milestone))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_milestone() {
        MatcherAssert.assertThat(issueDemilestoned, Matchers.is(Matchers.not(new IssueDemilestoned(userId, timestamp, Mockito
                .mock(Milestone.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        MatcherAssert.assertThat(issueDemilestoned.hashCode(), Matchers.is(new IssueDemilestoned(userId, timestamp, milestone).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        MatcherAssert.assertThat(issueDemilestoned.toString(), Matchers.startsWith("IssueDemilestoned"));
    }

}
