
package edu.stanford.protege.webprotege.obo;

import edu.stanford.protege.webprotege.common.ProjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.semanticweb.owlapi.model.OWLEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;

@RunWith(value = org.mockito.runners.MockitoJUnitRunner.class)
public class GetOboTermIdAction_TestCase {

    private GetOboTermIdAction getOboTermIdAction;
    private ProjectId projectId = ProjectId.generate();
    @Mock
    private OWLEntity term;

    @Before
    public void setUp() {
        getOboTermIdAction = GetOboTermIdAction.create(projectId, term);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        GetOboTermIdAction.create(null, term);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(getOboTermIdAction.getProjectId(), is(this.projectId));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_term_IsNull() {
        GetOboTermIdAction.create(projectId, null);
    }

    @Test
    public void shouldReturnSupplied_term() {
        assertThat(getOboTermIdAction.getTerm(), is(this.term));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(getOboTermIdAction, is(getOboTermIdAction));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(getOboTermIdAction.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(getOboTermIdAction, is(GetOboTermIdAction.create(projectId, term)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(getOboTermIdAction, is(not(GetOboTermIdAction.create(ProjectId.generate(), term))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_term() {
        assertThat(getOboTermIdAction, is(not(GetOboTermIdAction.create(projectId, mock(OWLEntity.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(getOboTermIdAction.hashCode(), is(GetOboTermIdAction.create(projectId, term).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(getOboTermIdAction.toString(), startsWith("GetOboTermIdAction"));
    }
}
