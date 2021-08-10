
package edu.stanford.protege.webprotege.itemlist;

import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.user.UserDetailsManager;
import edu.stanford.protege.webprotege.sharing.PersonId;
import edu.stanford.protege.webprotege.user.UserDetails;
import edu.stanford.protege.webprotege.common.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class GetPersonIdItemsActionHandler_TestCase {

    private GetPersonIdItemsActionHandler actionHandler;

    @Mock
    private UserDetailsManager userDetailsManager;

    private UserId
            johnSmith_UpperCase = UserId.valueOf("John Smith"),
            johnSmith_LowerCase = UserId.valueOf("john smith");

    @Mock
    private GetPersonIdItemsAction action;

    @Before
    public void setUp()
            throws Exception
    {
        actionHandler = new GetPersonIdItemsActionHandler(userDetailsManager);
        when(userDetailsManager.getUserDetails(johnSmith_UpperCase)).thenReturn(Optional.of(mock(UserDetails.class)));
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_userDetailsManager_IsNull() {
        new GetPersonIdItemsActionHandler(null);
    }

    @Test
    public void shouldOnlyMatchExact() {
        when(action.getItemNames()).thenReturn(Arrays.asList("John Smith"));
        GetPersonIdItemsResult result = actionHandler.execute(action, mock(ExecutionContext.class));
        assertThat(result.getItems(), hasItems(PersonId.get(johnSmith_UpperCase.id())));
    }

}
