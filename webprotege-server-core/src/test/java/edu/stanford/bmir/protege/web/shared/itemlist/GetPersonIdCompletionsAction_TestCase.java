
package edu.stanford.bmir.protege.web.shared.itemlist;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class GetPersonIdCompletionsAction_TestCase {

    private GetPersonIdCompletionsAction action;

    private String completionText = "The completionText";

    @Before
    public void setUp()
        throws Exception
    {
        action = GetPersonIdCompletionsAction.create(completionText);
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_completionText_IsNull() {
        GetPersonIdCompletionsAction.create(null);
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
        MatcherAssert.assertThat(action, Matchers.is(GetPersonIdCompletionsAction.create(completionText)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_completionText() {
        MatcherAssert.assertThat(action, Matchers.is(Matchers.not(GetPersonIdCompletionsAction.create("String-969b88c9-a6bc-4a84-ba12-81dab308731f"))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        MatcherAssert.assertThat(action.hashCode(), Matchers.is(GetPersonIdCompletionsAction.create(completionText).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        MatcherAssert.assertThat(action.toString(), Matchers.startsWith("GetPersonIdCompletionsAction"));
    }

}
