package edu.stanford.protege.webprotege.user;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.auth.AuthenticationManager;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.auth.Password;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class CreateUserAccountActionHandler_TestCase {

    private CreateUserAccountActionHandler handler;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private CreateUserAccountAction action;

    @Mock
    private ExecutionContext executionContext;

    @Mock
    private UserId userId;

    @Mock
    private EmailAddress emailAddress;

    @Mock
    private AccessManager accessManager;

    @Mock
    private Password password;

    @Before
    public void setUp() throws Exception {
        handler = new CreateUserAccountActionHandler(accessManager, authenticationManager);
        when(action.getUserId()).thenReturn(userId);
        when(action.getEmailAddress()).thenReturn(emailAddress);
        when(action.getPassword()).thenReturn(password);
    }

    @Test
    public void shouldCreateUserAccount() {
        handler.execute(action, executionContext);
        verify(authenticationManager, times(1)).registerUser(userId, emailAddress, password);
    }
}
