
package edu.stanford.protege.webprotege.obo;

import edu.stanford.protege.webprotege.project.ProjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class GetOboTermDefinitionAction_TestCase {

    private GetOboTermDefinitionAction getOboTermDefinitionAction;
    @Mock
    private ProjectId projectId;
    @Mock
    private OWLEntity term;

    @Before
    public void setUp() {
        getOboTermDefinitionAction = GetOboTermDefinitionAction.create(projectId, term);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        GetOboTermDefinitionAction.create(null, term);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(getOboTermDefinitionAction.getProjectId(), is(this.projectId));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_term_IsNull() {
        GetOboTermDefinitionAction.create(projectId, null);
    }

    @Test
    public void shouldReturnSupplied_term() {
        assertThat(getOboTermDefinitionAction.getTerm(), is(this.term));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(getOboTermDefinitionAction, is(getOboTermDefinitionAction));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(getOboTermDefinitionAction.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(getOboTermDefinitionAction, is(GetOboTermDefinitionAction.create(projectId, term)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(getOboTermDefinitionAction, is(not(GetOboTermDefinitionAction.create(mock(ProjectId.class), term))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_term() {
        assertThat(getOboTermDefinitionAction, is(not(GetOboTermDefinitionAction.create(projectId, mock(OWLEntity.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(getOboTermDefinitionAction.hashCode(), is(GetOboTermDefinitionAction.create(projectId, term).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(getOboTermDefinitionAction.toString(), startsWith("GetOboTermDefinitionAction"));
    }

}
