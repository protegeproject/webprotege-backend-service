
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

@RunWith(value = MockitoJUnitRunner.class)
public class SetOboTermCrossProductAction_TestCase {

    private SetOboTermCrossProductAction setOboTermCrossProductAction;
    private ProjectId projectId = ProjectId.generate();
    @Mock
    private OWLClass entity;
    @Mock
    private OBOTermCrossProduct crossProduct;

    @Before
    public void setUp() {
        setOboTermCrossProductAction = SetOboTermCrossProductAction.create(projectId, entity, crossProduct);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        SetOboTermCrossProductAction.create(null, entity, crossProduct);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(setOboTermCrossProductAction.getProjectId(), is(this.projectId));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_entity_IsNull() {
        SetOboTermCrossProductAction.create(projectId, null, crossProduct);
    }

    @Test
    public void shouldReturnSupplied_entity() {
        assertThat(setOboTermCrossProductAction.getEntity(), is(this.entity));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_crossProduct_IsNull() {
        SetOboTermCrossProductAction.create(projectId, entity, null);
    }

    @Test
    public void shouldReturnSupplied_crossProduct() {
        assertThat(setOboTermCrossProductAction.getCrossProduct(), is(this.crossProduct));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(setOboTermCrossProductAction, is(setOboTermCrossProductAction));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(setOboTermCrossProductAction.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(setOboTermCrossProductAction, is(SetOboTermCrossProductAction.create(projectId, entity, crossProduct)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(setOboTermCrossProductAction, is(not(SetOboTermCrossProductAction.create(ProjectId.generate(), entity, crossProduct))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_entity() {
        assertThat(setOboTermCrossProductAction, is(not(SetOboTermCrossProductAction.create(projectId, Mockito.mock(OWLClass.class), crossProduct))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_crossProduct() {
        assertThat(setOboTermCrossProductAction, is(not(SetOboTermCrossProductAction.create(projectId, entity, Mockito.mock(OBOTermCrossProduct.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(setOboTermCrossProductAction.hashCode(), is(SetOboTermCrossProductAction.create(projectId, entity, crossProduct)
                                                                                           .hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(setOboTermCrossProductAction.toString(), startsWith("SetOboTermCrossProductAction"));
    }

}
