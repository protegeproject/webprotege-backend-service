
package edu.stanford.protege.webprotege.change;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.revision.RevisionNumber;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class RevertRevisionAction_TestCase {

    private RevertRevisionAction revertRevisionAction;

    private ProjectId projectId = ProjectId.generate();

    @Mock
    private RevisionNumber revisionNumber;

    @Before
    public void setUp()
        throws Exception
    {
        revertRevisionAction = RevertRevisionAction.create(projectId, revisionNumber);
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        RevertRevisionAction.create(null, revisionNumber);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        MatcherAssert.assertThat(revertRevisionAction.getProjectId(), Matchers.is(this.projectId));
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_revisionNumber_IsNull() {
        RevertRevisionAction.create(projectId, null);
    }

    @Test
    public void shouldReturnSupplied_revisionNumber() {
        MatcherAssert.assertThat(revertRevisionAction.getRevisionNumber(), Matchers.is(this.revisionNumber));
    }

    @Test
    public void shouldBeEqualToSelf() {
        MatcherAssert.assertThat(revertRevisionAction, Matchers.is(revertRevisionAction));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        MatcherAssert.assertThat(revertRevisionAction.equals(null), Matchers.is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        MatcherAssert.assertThat(revertRevisionAction, Matchers.is(RevertRevisionAction.create(projectId, revisionNumber)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        MatcherAssert.assertThat(revertRevisionAction, Matchers.is(Matchers.not(RevertRevisionAction.create(ProjectId.generate(), revisionNumber))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_revisionNumber() {
        MatcherAssert.assertThat(revertRevisionAction, Matchers.is(Matchers.not(RevertRevisionAction.create(projectId, Mockito.mock(RevisionNumber.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        MatcherAssert.assertThat(revertRevisionAction.hashCode(), Matchers.is(RevertRevisionAction.create(projectId, revisionNumber)
                                                                                                  .hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        MatcherAssert.assertThat(revertRevisionAction.toString(), Matchers.startsWith("RevertRevisionAction"));
    }


}
