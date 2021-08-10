
package edu.stanford.protege.webprotege.obo;

import edu.stanford.protege.webprotege.common.ProjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(MockitoJUnitRunner.class)
public class GetOboTermSynonymsAction_TestCase {

    private GetOboTermSynonymsAction getOboTermSynonymsAction;
    private ProjectId projectId = ProjectId.generate();
    @Mock
    private OWLEntity entity;

    @Before
    public void setUp() {
        getOboTermSynonymsAction = GetOboTermSynonymsAction.create(projectId, entity);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        GetOboTermSynonymsAction.create(null, entity);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(getOboTermSynonymsAction.getProjectId(), is(this.projectId));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_entity_IsNull() {
        GetOboTermSynonymsAction.create(projectId, null);
    }

    @Test
    public void shouldReturnSupplied_entity() {
        assertThat(getOboTermSynonymsAction.getEntity(), is(this.entity));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(getOboTermSynonymsAction, is(getOboTermSynonymsAction));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(getOboTermSynonymsAction.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(getOboTermSynonymsAction, is(GetOboTermSynonymsAction.create(projectId, entity)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(getOboTermSynonymsAction, is(not(GetOboTermSynonymsAction.create(ProjectId.generate(), entity))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_entity() {
        assertThat(getOboTermSynonymsAction, is(not(GetOboTermSynonymsAction.create(projectId, Mockito.mock(OWLEntity.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(getOboTermSynonymsAction.hashCode(), is(GetOboTermSynonymsAction.create(projectId, entity).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(getOboTermSynonymsAction.toString(), startsWith("GetOboTermSynonymsAction"));
    }

}
