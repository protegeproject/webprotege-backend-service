
package edu.stanford.protege.webprotege.tag;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.color.Color;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class AddProjectTagAction_TestCase {

    private AddProjectTagAction addProjectTagAction;

    @Mock
    private ProjectId projectId;

    private String label = "The label";

    private String description = "The description";

    @Mock
    private Color color;

    @Mock
    private Color backgroundColor;

    @Before
    public void setUp() {
        addProjectTagAction = AddProjectTagAction.create(projectId, label, description, color, backgroundColor);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        AddProjectTagAction.create(null, label, description, color, backgroundColor);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(addProjectTagAction.getProjectId(), is(this.projectId));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_label_IsNull() {
        AddProjectTagAction.create(projectId, null, description, color, backgroundColor);
    }

    @Test
    public void shouldReturnSupplied_label() {
        assertThat(addProjectTagAction.getLabel(), is(this.label));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_description_IsNull() {
        AddProjectTagAction.create(projectId, label, null, color, backgroundColor);
    }

    @Test
    public void shouldReturnSupplied_description() {
        assertThat(addProjectTagAction.getDescription(), is(this.description));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_color_IsNull() {
        AddProjectTagAction.create(projectId, label, description, null, backgroundColor);
    }

    @Test
    public void shouldReturnSupplied_color() {
        assertThat(addProjectTagAction.getColor(), is(this.color));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_backgroundColor_IsNull() {
        AddProjectTagAction.create(projectId, label, description, color, null);
    }

    @Test
    public void shouldReturnSupplied_backgroundColor() {
        assertThat(addProjectTagAction.getBackgroundColor(), is(this.backgroundColor));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(addProjectTagAction, is(addProjectTagAction));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(addProjectTagAction.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(addProjectTagAction, is(AddProjectTagAction.create(projectId, label, description, color, backgroundColor)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(addProjectTagAction, is(not(AddProjectTagAction.create(mock(ProjectId.class), label, description, color, backgroundColor))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_label() {
        assertThat(addProjectTagAction, is(not(AddProjectTagAction.create(projectId, "String-3bba18b6-6903-4c92-8ffb-7b00cb3fef7a", description, color, backgroundColor))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_description() {
        assertThat(addProjectTagAction, is(not(AddProjectTagAction.create(projectId, label, "String-69702116-7cad-4810-b6f5-e844b8d51369", color, backgroundColor))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_color() {
        assertThat(addProjectTagAction, is(not(AddProjectTagAction.create(projectId, label, description, mock(Color.class), backgroundColor))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_backgroundColor() {
        assertThat(addProjectTagAction, is(not(AddProjectTagAction.create(projectId, label, description, color, mock(Color.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(addProjectTagAction.hashCode(), is(AddProjectTagAction.create(projectId, label, description, color, backgroundColor).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(addProjectTagAction.toString(), startsWith("AddProjectTagAction"));
    }
}
