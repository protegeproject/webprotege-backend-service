package edu.stanford.protege.webprotege.user;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.authorization.ApplicationResource;
import edu.stanford.protege.webprotege.authorization.Subject;
import edu.stanford.protege.webprotege.dispatch.ApplicationActionHandler;
import edu.stanford.protege.webprotege.dispatch.RequestContext;
import edu.stanford.protege.webprotege.dispatch.RequestValidator;
import edu.stanford.protege.webprotege.dispatch.validators.NullValidator;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-10-29
 */
public class GetAuthenticatedUserDetailsActionHandler implements ApplicationActionHandler<GetAuthenticatedUserDetailsRequest, GetAuthenticatedUserDetailsResponse> {

    private final AccessManager accessManager;

    private final UserDetailsManager userDetailsManager;

    public GetAuthenticatedUserDetailsActionHandler(AccessManager accessManager,
                                                    UserDetailsManager userDetailsManager) {
        this.accessManager = accessManager;
        this.userDetailsManager = userDetailsManager;
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
            var userDetails = userDetailsManager.getUserDetails(userId)
                                                .orElse(UserDetails.getUserDetails(userId,
                                                                                   userId.id(), Optional.empty()));
            var permittedActions = accessManager.getCapabilityClosure(Subject.forUser(userId), ApplicationResource.get(),
                    new edu.stanford.protege.webprotege.ipc.ExecutionContext(executionContext.userId(), executionContext.jwt(), executionContext.correlationId()));
            return new GetAuthenticatedUserDetailsResponse(userDetails, permittedActions);
        }
    }
}
