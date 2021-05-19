
package edu.stanford.protege.webprotege.tag;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collection;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class GetEntityTagsResult_TestCase {

    private GetEntityTagsResult result;

    private Collection<Tag> entityTags;

    private Collection<Tag> projectTags;

    @Before
    public void setUp() {
        entityTags = Collections.singletonList(mock(Tag.class));
        projectTags = Collections.singletonList(mock(Tag.class));
        result = GetEntityTagsResult.create(entityTags, projectTags);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_entityTags_IsNull() {
        GetEntityTagsResult.create(null, projectTags);
    }

    @Test
    public void shouldReturnSupplied_entityTags() {
        assertThat(result.getEntityTags(), is(this.entityTags));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectTags_IsNull() {
        GetEntityTagsResult.create(entityTags, null);
    }

    @Test
    public void shouldReturnSupplied_projectTags() {
        assertThat(result.getProjectTags(), is(this.projectTags));
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
        assertThat(result, is(GetEntityTagsResult.create(entityTags, projectTags)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_entityTags() {
        assertThat(result, is(Matchers.not(GetEntityTagsResult.create(Collections.singletonList(mock(Tag.class)), projectTags))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectTags() {
        assertThat(result, is(Matchers.not(GetEntityTagsResult.create(entityTags, Collections.singletonList(mock(Tag.class))))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(result.hashCode(), is(GetEntityTagsResult.create(entityTags, projectTags).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(result.toString(), Matchers.startsWith("GetEntityTagsResult"));
    }

}
