package edu.stanford.bmir.protege.web.server.auth;

import edu.stanford.bmir.protege.web.server.app.UserInSessionFactory;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.session.WebProtegeSession;
import edu.stanford.bmir.protege.web.server.user.UserActivityManager;
import edu.stanford.bmir.protege.web.shared.app.UserInSession;
import edu.stanford.bmir.protege.web.shared.auth.*;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class PerformLoginActionHandler_TestCase {

    private PerformLoginActionHandler handler;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PerformLoginAction action;

    @Mock
    private UserId userId;

    @Mock
    private ExecutionContext executionContext;

    @Mock
    private WebProtegeSession webProtegeSession;

    @Mock
    private UserActivityManager activityManager;

    @Mock
    private UserInSessionFactory userInSessionFactory;

    @Mock
    private UserInSession userInSession;

    @Mock
    private Password password;

    @Before
    public void setUp() throws Exception {
        handler = new PerformLoginActionHandler(activityManager,
                                                userInSessionFactory, authenticationManager);
        when(action.getUserId()).thenReturn(userId);
        when(userId.isGuest()).thenReturn(false);
        when(action.getPassword()).thenReturn(password);
        when(userInSessionFactory.getUserInSession(any())).thenReturn(userInSession);
        when(executionContext.getSession()).thenReturn(webProtegeSession);
    }

    @Test
    public void shouldFailAuthentication() {
        when(authenticationManager.authenticateUser(userId, password)).thenReturn(AuthenticationResponse.FAIL);
        var result = handler.execute(action, executionContext);
        assertThat(result.getAuthenticationResponse(), is(AuthenticationResponse.FAIL));
        verify(webProtegeSession, never()).setUserInSession(Mockito.any(UserId.class));
    }

    @Test
    public void shouldSetUserInSession() {
        when(authenticationManager.authenticateUser(userId, password)).thenReturn(AuthenticationResponse.SUCCESS);
        var result = handler.execute(action, executionContext);
        assertThat(result.getAuthenticationResponse(), is(AuthenticationResponse.SUCCESS));
        verify(webProtegeSession, times(1)).setUserInSession(Mockito.any(UserId.class));
    }
}
