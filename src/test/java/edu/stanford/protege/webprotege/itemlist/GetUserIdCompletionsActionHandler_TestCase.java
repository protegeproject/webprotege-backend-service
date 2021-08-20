
package edu.stanford.protege.webprotege.itemlist;

import edu.stanford.protege.webprotege.MockingUtils;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.user.UserDetailsManager;
import edu.stanford.protege.webprotege.common.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class GetUserIdCompletionsActionHandler_TestCase {

    private GetUserIdCompletionsActionHandler actionHandler;

    @Mock
    private UserDetailsManager userDetailsManager;

    private List<UserId> userIds;

    private UserId johnSmith = MockingUtils.mockUserId(), janeDoe = MockingUtils.mockUserId();

    @Mock
    private GetUserIdCompletionsAction action;

    @Before
    public void setUp() {
        actionHandler = new GetUserIdCompletionsActionHandler(userDetailsManager);
        userIds = Arrays.asList(johnSmith, janeDoe);
        when(userDetailsManager.getUserIdsContainingIgnoreCase(anyString(), anyInt())).thenReturn(userIds);
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_userDetailsManager_IsNull() {
        new GetUserIdCompletionsActionHandler(null);
    }

    @Test
    public void shouldReturnFoundUserIds() {
        when(action.getCompletionText()).thenReturn("j");
        GetUserIdCompletionsResult result = actionHandler.execute(action, mock(ExecutionContext.class));
        assertThat(result.getCompletions(), hasItems(janeDoe, johnSmith));
    }
}
