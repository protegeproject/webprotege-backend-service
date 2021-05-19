
package edu.stanford.protege.webprotege.perspective;

import edu.stanford.protege.webprotege.project.ProjectId;
import edu.stanford.protege.webprotege.user.UserId;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class SetPerspectiveLayoutAction_TestCase {

    private SetPerspectiveLayoutAction setPerspectiveLayoutAction;
    @Mock
    private ProjectId projectId;
    @Mock
    private UserId userId;
    @Mock
    private PerspectiveLayout layout;

    @Before
    public void setUp()
        throws Exception
    {
        setPerspectiveLayoutAction = SetPerspectiveLayoutAction.create(projectId, userId, layout);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        SetPerspectiveLayoutAction.create(null, userId, layout);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(setPerspectiveLayoutAction.getProjectId(), is(this.projectId));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_userId_IsNull() {
        SetPerspectiveLayoutAction.create(projectId, null, layout);
    }

    @Test
    public void shouldReturnSupplied_userId() {
        assertThat(setPerspectiveLayoutAction.getUserId(), is(this.userId));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_layout_IsNull() {
        SetPerspectiveLayoutAction.create(projectId, userId, null);
    }

    @Test
    public void shouldReturnSupplied_layout() {
        assertThat(setPerspectiveLayoutAction.getLayout(), is(this.layout));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(setPerspectiveLayoutAction, is(setPerspectiveLayoutAction));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(setPerspectiveLayoutAction.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(setPerspectiveLayoutAction, is(SetPerspectiveLayoutAction.create(projectId, userId, layout)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(setPerspectiveLayoutAction, is(Matchers.not(SetPerspectiveLayoutAction.create(mock(ProjectId.class), userId, layout))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_userId() {
        assertThat(setPerspectiveLayoutAction, is(Matchers.not(SetPerspectiveLayoutAction.create(projectId, mock(UserId.class), layout))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_layout() {
        assertThat(setPerspectiveLayoutAction, is(Matchers.not(SetPerspectiveLayoutAction.create(projectId, userId, mock(PerspectiveLayout.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(setPerspectiveLayoutAction.hashCode(), is(SetPerspectiveLayoutAction.create(projectId, userId, layout)
                                                                                       .hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(setPerspectiveLayoutAction.toString(), Matchers.startsWith("SetPerspectiveLayoutAction"));
    }

}
