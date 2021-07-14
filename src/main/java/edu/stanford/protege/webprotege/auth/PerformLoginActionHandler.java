package edu.stanford.protege.webprotege.auth;

import edu.stanford.protege.webprotege.app.UserInSessionFactory;
import edu.stanford.protege.webprotege.dispatch.ApplicationActionHandler;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.dispatch.RequestContext;
import edu.stanford.protege.webprotege.dispatch.RequestValidator;
import edu.stanford.protege.webprotege.dispatch.validators.NullValidator;
import edu.stanford.protege.webprotege.session.WebProtegeSession;
import edu.stanford.protege.webprotege.user.UserActivityManager;
import edu.stanford.protege.webprotege.user.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14/02/15
 */
public class PerformLoginActionHandler implements ApplicationActionHandler<PerformLoginAction, PerformLoginResult> {

    private static final Logger logger = LoggerFactory.getLogger(PerformLoginActionHandler.class);

    @Nonnull
    private final UserActivityManager activityManager;

    @Nonnull
    private final UserInSessionFactory userInSessionFactory;

    @Nonnull
    private final AuthenticationManager authenticationManager;

    @Inject
    public PerformLoginActionHandler(@Nonnull UserActivityManager activityManager,
                                     @Nonnull UserInSessionFactory userInSessionFactory,
                                     @Nonnull AuthenticationManager authenticationManager) {
        this.activityManager = checkNotNull(activityManager);
        this.userInSessionFactory = userInSessionFactory;
        this.authenticationManager = checkNotNull(authenticationManager);
    }

    @Nonnull
    @Override
    public Class<PerformLoginAction> getActionClass() {
        return PerformLoginAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull PerformLoginAction action, @Nonnull RequestContext requestContext) {
        return NullValidator.get();
    }

    @Nonnull
    @Override
    public PerformLoginResult execute(@Nonnull PerformLoginAction action, @Nonnull ExecutionContext executionContext) {
        WebProtegeSession session = executionContext.getSession();
        var userId = action.getUserId();
        if(userId.isGuest()) {
            logger.info("Attempt to login guest user");
            return getFailedLoginResult();
        }
        var authenticationResult = authenticationManager.authenticateUser(userId, action.getPassword());
        if(authenticationResult == AuthenticationResponse.SUCCESS) {
            session.setUserInSession(userId);
            activityManager.setLastLogin(userId, System.currentTimeMillis());
            logger.info("{} logged in (attached to session {})", userId, session.getSessionId());
            var userInSession = userInSessionFactory.getUserInSession(userId);
            return PerformLoginResult.create(AuthenticationResponse.SUCCESS, userInSession);
        }
        else {
            logger.info("Login attempt failed for user {}", userId);
            return getFailedLoginResult();
        }

    }

    @Nonnull
    private PerformLoginResult getFailedLoginResult() {
        return PerformLoginResult.create(AuthenticationResponse.FAIL, userInSessionFactory.getUserInSession(UserId.getGuest()));
    }
}
