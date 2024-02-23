package edu.stanford.protege.webprotege.mail;

import edu.stanford.protege.webprotege.dispatch.ApplicationActionHandler;
import edu.stanford.protege.webprotege.dispatch.RequestContext;
import edu.stanford.protege.webprotege.dispatch.RequestValidationResult;
import edu.stanford.protege.webprotege.dispatch.RequestValidator;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.user.EmailAddress;
import edu.stanford.protege.webprotege.user.UserDetailsManager;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/11/2013
 */
public class GetEmailAddressActionHandler implements ApplicationActionHandler<GetEmailAddressAction, GetEmailAddressResult> {

    private final UserDetailsManager userDetailsManager;

    @Inject
    public GetEmailAddressActionHandler(UserDetailsManager userDetailsManager) {
        this.userDetailsManager = userDetailsManager;
    }

    @Nonnull
    @Override
    public Class<GetEmailAddressAction> getActionClass() {
        return GetEmailAddressAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull GetEmailAddressAction action, @Nonnull RequestContext requestContext) {
        return () -> {
            if(!requestContext.getUserId().isGuest()) {
                return RequestValidationResult.getValid();
            }
            else {
                return RequestValidationResult.getInvalid("Cannot get the email address of the guest user");
            }
        };
    }

    @Nonnull
    @Override
    public GetEmailAddressResult execute(@Nonnull GetEmailAddressAction action, @Nonnull ExecutionContext executionContext) {
        Optional<EmailAddress> address = userDetailsManager.getEmail(action.userId()).map(EmailAddress::new);
        return new GetEmailAddressResult(action.userId(), address.orElse(null));
    }
}
