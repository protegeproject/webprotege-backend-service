
package edu.stanford.protege.webprotege.tag;

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
public class GetEntityTagsAction_TestCase {

    private GetEntityTagsAction action;

    @Mock
    private ProjectId projectId;

    @Mock
    private OWLEntity entity;

    @Before
    public void setUp() {
        action = GetEntityTagsAction.create(projectId, entity);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        GetEntityTagsAction.create(null, entity);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(action.getProjectId(), is(this.projectId));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_entity_IsNull() {
        GetEntityTagsAction.create(projectId, null);
    }

    @Test
    public void shouldReturnSupplied_entity() {
        assertThat(action.getEntity(), is(this.entity));
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
        assertThat(action, is(GetEntityTagsAction.create(projectId, entity)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(action, is(not(GetEntityTagsAction.create(mock(ProjectId.class), entity))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_entity() {
        assertThat(action, is(not(GetEntityTagsAction.create(projectId, mock(OWLEntity.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(action.hashCode(), is(GetEntityTagsAction.create(projectId, entity).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(action.toString(), startsWith("GetEntityTagsAction"));
    }

}
