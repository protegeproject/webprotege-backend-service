package edu.stanford.protege.webprotege.user;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.auth.AuthenticationManager;
import edu.stanford.protege.webprotege.dispatch.ApplicationActionHandler;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.dispatch.RequestContext;
import edu.stanford.protege.webprotege.dispatch.RequestValidator;
import edu.stanford.protege.webprotege.dispatch.validators.ApplicationPermissionValidator;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.protege.webprotege.access.BuiltInAction.CREATE_ACCOUNT;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/02/15
 */
public class CreateUserAccountActionHandler implements ApplicationActionHandler<CreateUserAccountAction, CreateUserAccountResult> {

    @Nonnull
    private final AccessManager accessManager;

    @Nonnull
    private final AuthenticationManager authenticationManager;

    @Inject
    public CreateUserAccountActionHandler(@Nonnull AccessManager accessManager,
                                          @Nonnull AuthenticationManager authenticationManager) {
        this.accessManager = checkNotNull(accessManager);
        this.authenticationManager = checkNotNull(authenticationManager);
    }

    @Nonnull
    @Override
    public Class<CreateUserAccountAction> getActionClass() {
        return CreateUserAccountAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull CreateUserAccountAction action, @Nonnull RequestContext requestContext) {
        return new ApplicationPermissionValidator(accessManager, requestContext.getUserId(), CREATE_ACCOUNT);
    }

    @Nonnull
    @Override
    public CreateUserAccountResult execute(@Nonnull CreateUserAccountAction action, @Nonnull ExecutionContext executionContext) {
        authenticationManager.registerUser(action.getUserId(), action.getEmailAddress(), action.getPassword());
        return CreateUserAccountResult.create();
    }
}
