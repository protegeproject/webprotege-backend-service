
package edu.stanford.protege.webprotege.user;

import edu.stanford.protege.webprotege.common.UserId;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GetUserIdCompletionsResult_TestCase {

    private GetUserIdCompletionsResult result;

    private UserId userId = edu.stanford.protege.webprotege.MockingUtils.mockUserId();

    private List<UserId> possibleItemCompletions;

    @BeforeEach
    public void setUp()
        throws Exception
    {
        possibleItemCompletions = Arrays.asList(userId);
        result = GetUserIdCompletionsResult.create(possibleItemCompletions);
    }

    public void shouldThrowNullPointerExceptionIf_possibleItemCompletions_IsNull() {
        assertThrows(NullPointerException.class, () -> {
            GetUserIdCompletionsResult.create(null);
        });
    }


    @Test
    public void shouldBeEqualToSelf() {
        MatcherAssert.assertThat(result, Matchers.is(result));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        MatcherAssert.assertThat(result.equals(null), Matchers.is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        MatcherAssert.assertThat(result, Matchers.is(GetUserIdCompletionsResult.create(possibleItemCompletions)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_possibleItemCompletions() {
        MatcherAssert.assertThat(result, Matchers.is(Matchers.not(GetUserIdCompletionsResult.create(Arrays.asList(edu.stanford.protege.webprotege.MockingUtils.mockUserId())))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        MatcherAssert.assertThat(result.hashCode(), Matchers.is(GetUserIdCompletionsResult.create(possibleItemCompletions)
                                                                                          .hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        MatcherAssert.assertThat(result.toString(), Matchers.startsWith("GetUserIdCompletionsResult"));
    }

}
