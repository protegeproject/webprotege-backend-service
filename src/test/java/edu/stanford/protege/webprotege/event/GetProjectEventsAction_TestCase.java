
package edu.stanford.protege.webprotege.event;

import edu.stanford.protege.webprotege.project.ProjectId;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class GetProjectEventsAction_TestCase {

    private GetProjectEventsAction action;

    @Mock
    private EventTag sinceTag;

    @Mock
    private ProjectId projectId;

    @Before
    public void setUp() {
        action = GetProjectEventsAction.create(sinceTag, projectId);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_sinceTag_IsNull() {
        GetProjectEventsAction.create(null, projectId);
    }

    @Test
    public void shouldReturnSupplied_sinceTag() {
        assertThat(action.getSinceTag(), is(this.sinceTag));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        GetProjectEventsAction.create(sinceTag, null);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(action.getProjectId(), is(this.projectId));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(action, is(action));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(action.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(action, is(GetProjectEventsAction.create(sinceTag, projectId)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_sinceTag() {
        assertThat(action, is(not(GetProjectEventsAction.create(mock(EventTag.class), projectId))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(action, is(not(GetProjectEventsAction.create(sinceTag, mock(ProjectId.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(action.hashCode(), is(GetProjectEventsAction.create(sinceTag, projectId).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(action.toString(), Matchers.startsWith("GetProjectEventsAction"));
    }
}
