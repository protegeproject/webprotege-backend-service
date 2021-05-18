
package edu.stanford.bmir.protege.web.shared.itemlist;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class GetUserIdCompletionsAction_TestCase {

    private GetUserIdCompletionsAction action;

    private String completionText = "The completionText";

    @Before
    public void setUp()
        throws Exception
    {
        action = GetUserIdCompletionsAction.create(completionText);
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_completionText_IsNull() {
        GetUserIdCompletionsAction.create(null);
    }

    @Test
    public void shouldBeEqualToSelf() {
        MatcherAssert.assertThat(action, Matchers.is(action));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        MatcherAssert.assertThat(action.equals(null), Matchers.is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        MatcherAssert.assertThat(action, Matchers.is(GetUserIdCompletionsAction.create(completionText)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_completionText() {
        MatcherAssert.assertThat(action, Matchers.is(Matchers.not(GetUserIdCompletionsAction.create("String-629f6902-da3d-4a0e-ba06-71db635c07df"))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        MatcherAssert.assertThat(action.hashCode(), Matchers.is(GetUserIdCompletionsAction.create(completionText).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        MatcherAssert.assertThat(action.toString(), Matchers.startsWith("GetUserIdCompletionsAction"));
    }

}
