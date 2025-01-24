
package edu.stanford.protege.webprotege.place;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.perspective.PerspectiveId;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ProjectViewPlace_TestCase {

    private ProjectViewPlace projectViewPlace;

    private ProjectId projectId = ProjectId.generate();

    @Mock
    private PerspectiveId perspectiveId;

    @Mock
    private ItemSelection itemSelection;

    @BeforeEach
    public void setUp() throws Exception {
        projectViewPlace = new ProjectViewPlace(projectId, perspectiveId, itemSelection);
    }

    @Test
public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
    assertThrows(java.lang.NullPointerException.class, () -> { 
        new ProjectViewPlace(null, perspectiveId, itemSelection);
     });
}

    @Test
    public void shouldReturnSupplied_projectId() {
        MatcherAssert.assertThat(projectViewPlace.projectId(), Matchers.is(this.projectId));
    }

    @Test
public void shouldThrowNullPointerExceptionIf_perspectiveId_IsNull() {
    assertThrows(java.lang.NullPointerException.class, () -> { 
        new ProjectViewPlace(projectId, null, itemSelection);
     });
}

    @Test
    public void shouldReturnSupplied_perspectiveId() {
        MatcherAssert.assertThat(projectViewPlace.getPerspectiveId(), Matchers.is(this.perspectiveId));
    }

    @Test
    public void shouldBeEqualToSelf() {
        MatcherAssert.assertThat(projectViewPlace, Matchers.is(projectViewPlace));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        MatcherAssert.assertThat(projectViewPlace.equals(null), Matchers.is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        MatcherAssert.assertThat(projectViewPlace, Matchers.is(new ProjectViewPlace(projectId, perspectiveId, itemSelection)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        MatcherAssert.assertThat(projectViewPlace, Matchers.is(Matchers.not(new ProjectViewPlace(ProjectId.generate(), perspectiveId, itemSelection))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_perspectiveId() {
        MatcherAssert.assertThat(projectViewPlace, Matchers.is(Matchers.not(new ProjectViewPlace(projectId, Mockito.mock(PerspectiveId.class), itemSelection))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        MatcherAssert.assertThat(projectViewPlace.hashCode(), Matchers.is(new ProjectViewPlace(projectId, perspectiveId, itemSelection).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        MatcherAssert.assertThat(projectViewPlace.toString(), Matchers.startsWith("ProjectViewPlace"));
    }

    @Test
    public void should_getItemSelection() {
        MatcherAssert.assertThat(projectViewPlace.getItemSelection(), Matchers.is(itemSelection));
    }
}
