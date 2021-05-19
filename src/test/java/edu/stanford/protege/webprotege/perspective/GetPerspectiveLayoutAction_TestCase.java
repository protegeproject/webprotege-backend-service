
package edu.stanford.protege.webprotege.perspective;

import edu.stanford.protege.webprotege.project.ProjectId;
import edu.stanford.protege.webprotege.user.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class GetPerspectiveLayoutAction_TestCase {

    private GetPerspectiveLayoutAction getPerspectiveLayoutAction;

    @Mock
    private ProjectId projectId;

    @Mock
    private UserId userId;

    @Mock
    private PerspectiveId perspectiveId;

    @Before
    public void setUp() {
        getPerspectiveLayoutAction = GetPerspectiveLayoutAction.create(projectId, userId, perspectiveId);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        GetPerspectiveLayoutAction.create(null, userId, perspectiveId);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(getPerspectiveLayoutAction.getProjectId(), is(this.projectId));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_userId_IsNull() {
        GetPerspectiveLayoutAction.create(projectId, null, perspectiveId);
    }

    @Test
    public void shouldReturnSupplied_userId() {
        assertThat(getPerspectiveLayoutAction.getUserId(), is(this.userId));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_perspectiveId_IsNull() {
        GetPerspectiveLayoutAction.create(projectId, userId, null);
    }

    @Test
    public void shouldReturnSupplied_perspectiveId() {
        assertThat(getPerspectiveLayoutAction.getPerspectiveId(), is(this.perspectiveId));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(getPerspectiveLayoutAction, is(getPerspectiveLayoutAction));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(getPerspectiveLayoutAction.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(getPerspectiveLayoutAction, is(GetPerspectiveLayoutAction.create(projectId, userId, perspectiveId)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(getPerspectiveLayoutAction, is(not(GetPerspectiveLayoutAction.create(Mockito.mock(ProjectId.class), userId, perspectiveId))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_userId() {
        assertThat(getPerspectiveLayoutAction, is(not(GetPerspectiveLayoutAction.create(projectId, Mockito.mock(UserId.class), perspectiveId))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_perspectiveId() {
        assertThat(getPerspectiveLayoutAction, is(not(GetPerspectiveLayoutAction.create(projectId, userId, Mockito.mock(PerspectiveId.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(getPerspectiveLayoutAction.hashCode(), is(GetPerspectiveLayoutAction.create(projectId, userId, perspectiveId)
                                                                                       .hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(getPerspectiveLayoutAction.toString(), startsWith("GetPerspectiveLayoutAction"));
    }

}
