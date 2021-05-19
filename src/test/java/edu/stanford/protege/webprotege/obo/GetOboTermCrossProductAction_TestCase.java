
package edu.stanford.protege.webprotege.obo;

import edu.stanford.protege.webprotege.project.ProjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLClass;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(MockitoJUnitRunner.class)
public class GetOboTermCrossProductAction_TestCase {

    private GetOboTermCrossProductAction getOboTermCrossProductAction;
    @Mock
    private ProjectId projectId;
    @Mock
    private OWLClass entity;

    @Before
    public void setUp() {
        getOboTermCrossProductAction = GetOboTermCrossProductAction.create(projectId, entity);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        GetOboTermCrossProductAction.create(null, entity);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(getOboTermCrossProductAction.getProjectId(), is(this.projectId));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_entity_IsNull() {
        GetOboTermCrossProductAction.create(projectId, null);
    }

    @Test
    public void shouldReturnSupplied_entity() {
        assertThat(getOboTermCrossProductAction.getEntity(), is(this.entity));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(getOboTermCrossProductAction, is(getOboTermCrossProductAction));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(getOboTermCrossProductAction.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(getOboTermCrossProductAction, is(GetOboTermCrossProductAction.create(projectId, entity)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(getOboTermCrossProductAction, is(not(GetOboTermCrossProductAction.create(Mockito.mock(ProjectId.class), entity))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_entity() {
        assertThat(getOboTermCrossProductAction, is(not(GetOboTermCrossProductAction.create(projectId, Mockito.mock(OWLClass.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(getOboTermCrossProductAction.hashCode(), is(GetOboTermCrossProductAction.create(projectId, entity).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(getOboTermCrossProductAction.toString(), startsWith("GetOboTermCrossProductAction"));
    }

}
