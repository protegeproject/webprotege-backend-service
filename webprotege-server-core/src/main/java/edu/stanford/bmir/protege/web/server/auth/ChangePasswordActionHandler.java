package edu.stanford.bmir.protege.web.server.auth;

import edu.stanford.bmir.protege.web.server.dispatch.ApplicationActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.shared.auth.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/02/15
 */
public class ChangePasswordActionHandler implements ApplicationActionHandler<ChangePasswordAction, ChangePasswordResult> {

    private final AuthenticationManager authenticationManager;

    @Inject
    public ChangePasswordActionHandler(AuthenticationManager authMan) {
        this.authenticationManager = authMan;
    }

    @Nonnull
    @Override
    public Class<ChangePasswordAction> getActionClass() {
        return ChangePasswordAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull ChangePasswordAction action, @Nonnull RequestContext requestContext) {
        return NullValidator.get();
    }

    @Nonnull
    @Override
    public ChangePasswordResult execute(@Nonnull ChangePasswordAction action,
                                        @Nonnull ExecutionContext executionContext) {
        var userId = action.getUserId();
        var authResponse = authenticationManager.authenticateUser(userId,
                                                                  action.getCurrentPassword());
        if(authResponse == AuthenticationResponse.FAIL) {
            return ChangePasswordResult.create(AuthenticationResponse.FAIL);
        }
        authenticationManager.setPassword(userId, action.getNewPassword());
        return ChangePasswordResult.create(AuthenticationResponse.SUCCESS);
    }
}
