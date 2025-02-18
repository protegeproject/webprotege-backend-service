
package edu.stanford.protege.webprotege.access;

import edu.stanford.protege.webprotege.authorization.ApplicationResource;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ApplicationResource_TestCase {

    private ApplicationResource applicationResource;

    @BeforeEach
    public void setUp() {
        applicationResource = ApplicationResource.get();
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(applicationResource, is(applicationResource));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(applicationResource.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(applicationResource, is(ApplicationResource.get()));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(applicationResource.hashCode(), is(ApplicationResource.get().hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(applicationResource.toString(), Matchers.startsWith("ApplicationResource"));
    }

    @Test
    public void should_getEmptyProjectId() {
        assertThat(applicationResource.getProjectId(), is(Optional.empty()));
    }

    @Test
    public void shouldReturn_true_For_isApplicationTarget() {
        assertThat(applicationResource.isApplication(), is(true));
    }

    @Test
    public void shouldReturn_false_For_isProjectTarget() {
        assertThat(applicationResource.isProject(ProjectId.generate()), is(false));
    }

}
