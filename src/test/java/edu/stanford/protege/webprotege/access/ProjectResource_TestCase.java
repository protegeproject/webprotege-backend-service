
package edu.stanford.protege.webprotege.access;

import edu.stanford.protege.webprotege.authorization.ProjectResource;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ProjectResource_TestCase {

    private ProjectResource projectResource;

    private ProjectId projectId = ProjectId.generate();

    @BeforeEach
    public void setUp() {
        projectResource = new ProjectResource(projectId);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
    assertThrows(NullPointerException.class, () -> { 
        new ProjectResource(null);
     });
}

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(projectResource.getProjectId(), is(Optional.of(this.projectId)));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(projectResource, is(projectResource));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(projectResource.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(projectResource, is(new ProjectResource(projectId)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(projectResource, is(not(new ProjectResource(ProjectId.generate()))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(projectResource.hashCode(), is(new ProjectResource(projectId).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(projectResource.toString(), startsWith("ProjectResource"));
    }

    @Test
    public void shouldReturn_true_For_isProjectTarget() {
        assertThat(projectResource.isProject(projectId), is(true));
    }

    @Test
    public void shouldReturn_false_For_isProjectTarget() {
        assertThat(projectResource.isProject(ProjectId.generate()), is(false));
    }

    @Test
    public void shouldReturn_false_For_isApplicationTarget() {
        assertThat(projectResource.isApplication(), is(false));
    }

}
