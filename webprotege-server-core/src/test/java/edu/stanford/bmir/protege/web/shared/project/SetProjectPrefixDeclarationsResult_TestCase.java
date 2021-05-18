
package edu.stanford.bmir.protege.web.shared.project;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class SetProjectPrefixDeclarationsResult_TestCase {

    private SetProjectPrefixDeclarationsResult result;

    @Mock
    private ProjectId projectId;

    private List<PrefixDeclaration> prefixDeclarations;

    @Before
    public void setUp() {
        prefixDeclarations = new ArrayList<>();
        prefixDeclarations.add(mock(PrefixDeclaration.class));
        result = SetProjectPrefixDeclarationsResult.create(projectId, prefixDeclarations);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        SetProjectPrefixDeclarationsResult.create(null, prefixDeclarations);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(result.getProjectId(), is(this.projectId));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_prefixDeclarations_IsNull() {
        SetProjectPrefixDeclarationsResult.create(projectId, null);
    }

    @Test
    public void shouldReturnSupplied_prefixDeclarations() {
        assertThat(result.getPrefixDeclarations(), is(this.prefixDeclarations));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(result, is(result));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(result.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(result, is(SetProjectPrefixDeclarationsResult.create(projectId, prefixDeclarations)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(result, is(not(SetProjectPrefixDeclarationsResult.create(mock(ProjectId.class), prefixDeclarations))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_prefixDeclarations() {
        List<PrefixDeclaration> otherDecls = new ArrayList<>();
        otherDecls.add(mock(PrefixDeclaration.class));
        assertThat(result, is(Matchers.<SetProjectPrefixDeclarationsResult>not(SetProjectPrefixDeclarationsResult.create(projectId, otherDecls))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(result.hashCode(), is(SetProjectPrefixDeclarationsResult.create(projectId, prefixDeclarations).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(result.toString(), startsWith("SetProjectPrefixDeclarationsResult"));
    }

}
