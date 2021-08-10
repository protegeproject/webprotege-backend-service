
package edu.stanford.protege.webprotege.obo;

import edu.stanford.protege.webprotege.common.ProjectId;
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
public class GetOboTermRelationshipsAction_TestCase {

    private GetOboTermRelationshipsAction getOboTermRelationshipsAction;
    private ProjectId projectId = ProjectId.generate();
    @Mock
    private OWLClass entity;

    @Before
    public void setUp() {
        getOboTermRelationshipsAction = GetOboTermRelationshipsAction.create(projectId, entity);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        GetOboTermRelationshipsAction.create(null, entity);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(getOboTermRelationshipsAction.getProjectId(), is(this.projectId));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_entity_IsNull() {
        GetOboTermRelationshipsAction.create(projectId, null);
    }

    @Test
    public void shouldReturnSupplied_entity() {
        assertThat(getOboTermRelationshipsAction.getEntity(), is(this.entity));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(getOboTermRelationshipsAction, is(getOboTermRelationshipsAction));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(getOboTermRelationshipsAction.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(getOboTermRelationshipsAction, is(GetOboTermRelationshipsAction.create(projectId, entity)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(getOboTermRelationshipsAction, is(not(GetOboTermRelationshipsAction.create(ProjectId.generate(), entity))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_entity() {
        assertThat(getOboTermRelationshipsAction, is(not(GetOboTermRelationshipsAction.create(projectId, Mockito.mock(OWLClass.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(getOboTermRelationshipsAction.hashCode(), is(GetOboTermRelationshipsAction.create(projectId, entity).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(getOboTermRelationshipsAction.toString(), startsWith("GetOboTermRelationshipsAction"));
    }

}
