package edu.stanford.protege.webprotege.user;

import edu.stanford.protege.webprotege.app.UserInSessionFactory;
import edu.stanford.protege.webprotege.dispatch.ApplicationActionHandler;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.dispatch.RequestContext;
import edu.stanford.protege.webprotege.dispatch.RequestValidator;
import edu.stanford.protege.webprotege.dispatch.validators.NullValidator;
import edu.stanford.protege.webprotege.session.WebProtegeSession;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/02/15
 */
public class LogOutUserActionHandler implements ApplicationActionHandler<LogOutUserAction, LogOutUserResult> {

    @Nonnull
    private final UserActivityManager userActivityManager;

    @Nonnull
    private final UserInSessionFactory userInSessionFactory;

    @Inject
    public LogOutUserActionHandler(@Nonnull UserActivityManager userActivityManager,
                                   @Nonnull UserInSessionFactory userInSessionFactory) {
        this.userActivityManager = checkNotNull(userActivityManager);
        this.userInSessionFactory = checkNotNull(userInSessionFactory);
    }

    @Nonnull
    @Override
    public Class<LogOutUserAction> getActionClass() {
        return LogOutUserAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull LogOutUserAction action, @Nonnull RequestContext requestContext) {
        return NullValidator.get();
    }

    @Nonnull
    @Override
    public LogOutUserResult execute(@Nonnull LogOutUserAction action, @Nonnull ExecutionContext executionContext) {
        WebProtegeSession session = executionContext.getSession();
        UserId userId = session.getUserInSession();
        session.clearUserInSession();
        if (!userId.isGuest()) {
            userActivityManager.setLastLogout(userId, System.currentTimeMillis());
        }
        return new LogOutUserResult(userInSessionFactory.getUserInSession(UserId.getGuest()));
    }
}
