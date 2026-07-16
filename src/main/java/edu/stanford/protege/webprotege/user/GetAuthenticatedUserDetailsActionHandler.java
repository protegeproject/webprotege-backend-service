package edu.stanford.protege.webprotege.user;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.authorization.ApplicationResource;
import edu.stanford.protege.webprotege.authorization.Subject;
import edu.stanford.protege.webprotege.dispatch.ApplicationActionHandler;
import edu.stanford.protege.webprotege.dispatch.RequestContext;
import edu.stanford.protege.webprotege.dispatch.RequestValidator;
import edu.stanford.protege.webprotege.dispatch.validators.NullValidator;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.sharing.PendingSharingInvitationRedeemer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-10-29
 */
public class GetAuthenticatedUserDetailsActionHandler implements ApplicationActionHandler<GetAuthenticatedUserDetailsRequest, GetAuthenticatedUserDetailsResponse> {

    private static final Logger logger = LoggerFactory.getLogger(GetAuthenticatedUserDetailsActionHandler.class);

    private final AccessManager accessManager;

    private final UserDetailsManager userDetailsManager;

    private final PendingSharingInvitationRedeemer invitationRedeemer;

    public GetAuthenticatedUserDetailsActionHandler(AccessManager accessManager,
                                                    UserDetailsManager userDetailsManager,
                                                    PendingSharingInvitationRedeemer invitationRedeemer) {
        this.accessManager = accessManager;
        this.userDetailsManager = userDetailsManager;
        this.invitationRedeemer = invitationRedeemer;
    }

    @Nonnull
    @Override
    public Class<GetAuthenticatedUserDetailsRequest> getActionClass() {
        return GetAuthenticatedUserDetailsRequest.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull GetAuthenticatedUserDetailsRequest action,
                                                @Nonnull RequestContext requestContext) {
        return NullValidator.get();
    }

    @Nonnull
    @Override
    public GetAuthenticatedUserDetailsResponse execute(@Nonnull GetAuthenticatedUserDetailsRequest action,
                                                       @Nonnull ExecutionContext executionContext) {

        var userId = executionContext.userId();
        if(userId.isGuest()) {
            return new GetAuthenticatedUserDetailsResponse(UserDetails.getGuestUserDetails(),
                                                           Collections.emptySet());
        }
        else {
            // Redeem any pending sharing invitations for this user before computing their capability
            // closure, so freshly granted project access is reflected in this same response. This must
            // never break login, so any failure is caught and logged rather than propagated.
            try {
                invitationRedeemer.redeem(userId, executionContext.jwt());
            } catch (RuntimeException e) {
                logger.error("Could not redeem pending sharing invitations for user {}: {}",
                             userId, e.toString());
            }
            var userDetails = userDetailsManager.getUserDetails(userId)
                                                .orElse(UserDetails.getUserDetails(userId,
                                                                                   userId.id(), Optional.empty()));
            var permittedActions = accessManager.getCapabilityClosure(Subject.forUser(userId), ApplicationResource.get(),
                    new edu.stanford.protege.webprotege.ipc.ExecutionContext(executionContext.userId(), executionContext.jwt(), executionContext.correlationId()));
            return new GetAuthenticatedUserDetailsResponse(userDetails, permittedActions);
        }
    }
}
