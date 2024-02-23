package edu.stanford.protege.webprotege.mail;

import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.dispatch.ApplicationActionHandler;
import edu.stanford.protege.webprotege.dispatch.RequestContext;
import edu.stanford.protege.webprotege.dispatch.RequestValidator;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.user.EmailAddress;
import edu.stanford.protege.webprotege.user.UserDetailsManager;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static edu.stanford.protege.webprotege.mail.SetEmailAddressResult.Result.ADDRESS_ALREADY_EXISTS;
import static edu.stanford.protege.webprotege.mail.SetEmailAddressResult.Result.ADDRESS_CHANGED;
import static edu.stanford.protege.webprotege.mail.SetEmailAddressResult.Result.ADDRESS_UNCHANGED;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/11/2013
 */
public class SetEmailAddressActionHandler implements ApplicationActionHandler<SetEmailAddressAction, SetEmailAddressResult> {

    private final UserDetailsManager userDetailsManager;

    @Inject
    public SetEmailAddressActionHandler(UserDetailsManager userDetailsManager) {
        this.userDetailsManager = userDetailsManager;
    }

    @Nonnull
    @Override
    public Class<SetEmailAddressAction> getActionClass() {
        return SetEmailAddressAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull SetEmailAddressAction action, @Nonnull RequestContext requestContext) {
        return new SetEmailAddressRequestValidator(action.userId(), requestContext.getUserId());
    }

    @Nonnull
    @Override
    public SetEmailAddressResult execute(@Nonnull SetEmailAddressAction action, @Nonnull ExecutionContext executionContext) {
        String emailAddress = action.emailAddress().getEmailAddress();
        Optional<UserId> userId = userDetailsManager.getUserIdByEmailAddress(new EmailAddress(emailAddress));
        if(userId.isPresent()) {
            if(userId.get().equals(action.userId())) {
                // Same user, same address
                return new SetEmailAddressResult(ADDRESS_UNCHANGED);
            }
            else {
                // Already exists
                return new SetEmailAddressResult(ADDRESS_ALREADY_EXISTS);
            }
        }
        else {
            userDetailsManager.setEmail(action.userId(), emailAddress);
            return new SetEmailAddressResult(ADDRESS_CHANGED);
        }
    }
}
