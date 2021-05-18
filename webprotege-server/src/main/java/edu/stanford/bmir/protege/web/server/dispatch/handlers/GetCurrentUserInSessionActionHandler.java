package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.server.app.UserInSessionFactory;
import edu.stanford.bmir.protege.web.server.dispatch.*;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.app.UserInSession;
import edu.stanford.bmir.protege.web.server.user.GetCurrentUserInSessionAction;
import edu.stanford.bmir.protege.web.server.user.GetCurrentUserInSessionResult;
import edu.stanford.bmir.protege.web.server.user.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/04/2013
 */
public class GetCurrentUserInSessionActionHandler implements ApplicationActionHandler<GetCurrentUserInSessionAction, GetCurrentUserInSessionResult> {

    @Nonnull
    private final UserInSessionFactory userInSessionFactory;

    @Inject
    public GetCurrentUserInSessionActionHandler(@Nonnull UserInSessionFactory userInSessionFactory) {
        this.userInSessionFactory = checkNotNull(userInSessionFactory);
    }

    /**
     * Gets the class of {@link Action} handled by this handler.
     * @return The class of {@link Action}.  Not {@code null}.
     */
    @Nonnull
    @Override
    public Class<GetCurrentUserInSessionAction> getActionClass() {
        return GetCurrentUserInSessionAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull GetCurrentUserInSessionAction action, @Nonnull RequestContext requestContext) {
        return NullValidator.get();
    }

    @Nonnull
    @Override
    public GetCurrentUserInSessionResult execute(@Nonnull GetCurrentUserInSessionAction action, @Nonnull ExecutionContext executionContext) {
        UserId userId = executionContext.getUserId();
        UserInSession userInSession = userInSessionFactory.getUserInSession(userId);
        return GetCurrentUserInSessionResult.create(userInSession);
    }
}
