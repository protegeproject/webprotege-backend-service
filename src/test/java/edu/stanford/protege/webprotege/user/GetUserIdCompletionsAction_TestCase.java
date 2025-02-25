
package edu.stanford.protege.webprotege.user;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GetUserIdCompletionsAction_TestCase {

    private GetUserIdCompletionsAction action;

    private String completionText = "The completionText";

    @BeforeEach
    public void setUp()
        throws Exception
    {
        action = GetUserIdCompletionsAction.create(completionText);
    }

    @Test
public void shouldThrowNullPointerExceptionIf_completionText_IsNull() {
    assertThrows(java.lang.NullPointerException.class, () -> { 
        GetUserIdCompletionsAction.create(null);
     });
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
