
package edu.stanford.protege.webprotege.user;

import edu.stanford.protege.webprotege.MockingUtils;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GetUserIdCompletionsActionHandler_TestCase {

    private GetUserIdCompletionsActionHandler actionHandler;
    private ExecutionContext executionContext = new ExecutionContext(new UserId("1"), "DUMMY_JWT");

    @Mock
    private UserDetailsManager userDetailsManager;

    private List<UserId> userIds;

    private UserId johnSmith = MockingUtils.mockUserId(), janeDoe = MockingUtils.mockUserId();

    @Mock
    private GetUserIdCompletionsAction action;

    @BeforeEach
    public void setUp() {
        actionHandler = new GetUserIdCompletionsActionHandler(userDetailsManager);
        userIds = Arrays.asList(johnSmith, janeDoe);
        when(userDetailsManager.getUserIdsContainingIgnoreCase(anyString(), anyInt())).thenReturn(userIds);
    }

    @Test
public void shouldThrowNullPointerExceptionIf_userDetailsManager_IsNull() {
    assertThrows(java.lang.NullPointerException.class, () -> { 
        new GetUserIdCompletionsActionHandler(null);
     });
}

    @Test
    public void shouldReturnFoundUserIds() {
        when(action.getCompletionText()).thenReturn("j");
        GetUserIdCompletionsResult result = actionHandler.execute(action, executionContext);
        assertThat(result.getCompletions(), hasItems(janeDoe, johnSmith));
    }
}
