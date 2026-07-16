package edu.stanford.protege.webprotege.user;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.authorization.Capability;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.sharing.PendingSharingInvitationRedeemer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Covers the sharing-invitation redemption hook added to
 * {@link GetAuthenticatedUserDetailsActionHandler}: redemption runs before the capability closure is
 * computed, is skipped for guests, and never breaks login when it fails.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GetAuthenticatedUserDetailsActionHandler_TestCase {

    @Mock
    private AccessManager accessManager;

    @Mock
    private UserDetailsManager userDetailsManager;

    @Mock
    private PendingSharingInvitationRedeemer invitationRedeemer;

    private GetAuthenticatedUserDetailsActionHandler handler;

    private final UserId userId = UserId.valueOf("jdoe");

    @BeforeEach
    public void setUp() {
        handler = new GetAuthenticatedUserDetailsActionHandler(accessManager, userDetailsManager, invitationRedeemer);
        when(userDetailsManager.getUserDetails(any())).thenReturn(Optional.empty());
        when(accessManager.getCapabilityClosure(any(), any(), any())).thenReturn(Collections.<Capability>emptySet());
    }

    @Test
    public void shouldRedeemInvitationsBeforeComputingCapabilityClosure() {
        ExecutionContext executionContext = new ExecutionContext(userId, "jwt-token", "corr-id");

        handler.execute(new GetAuthenticatedUserDetailsRequest(), executionContext);

        InOrder inOrder = inOrder(invitationRedeemer, accessManager);
        inOrder.verify(invitationRedeemer).redeem(userId, "jwt-token");
        inOrder.verify(accessManager).getCapabilityClosure(any(), any(), any());
    }

    @Test
    public void shouldSkipRedemptionForTheGuestUser() {
        ExecutionContext executionContext = new ExecutionContext(UserId.getGuest(), "jwt-token", "corr-id");

        var response = handler.execute(new GetAuthenticatedUserDetailsRequest(), executionContext);

        verify(invitationRedeemer, never()).redeem(any(), any());
        assertThat(response.userDetails(), is(UserDetails.getGuestUserDetails()));
    }

    @Test
    public void shouldStillReturnDetailsWhenRedemptionFails() {
        doThrow(new RuntimeException("redeemer blew up")).when(invitationRedeemer).redeem(any(), any());
        ExecutionContext executionContext = new ExecutionContext(userId, "jwt-token", "corr-id");

        var response = assertDoesNotThrow(
                () -> handler.execute(new GetAuthenticatedUserDetailsRequest(), executionContext));

        assertThat(response, is(notNullValue()));
        // Login proceeds: the capability closure is still computed and returned.
        verify(accessManager).getCapabilityClosure(any(), any(), any());
    }
}
