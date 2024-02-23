package edu.stanford.protege.webprotege.dispatch.validators;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.authorization.ActionId;
import edu.stanford.protege.webprotege.authorization.ApplicationResource;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.dispatch.RequestValidationResult;
import edu.stanford.protege.webprotege.dispatch.RequestValidator;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.protege.webprotege.authorization.Subject.forUser;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Mar 2017
 */
public class ApplicationPermissionValidator implements RequestValidator {

    private final AccessManager accessManager;

    private final UserId userId;

    private final ActionId actionId;

    private final ExecutionContext executionContext;

    public ApplicationPermissionValidator(@Nonnull AccessManager accessManager,
                                          @Nonnull UserId userId,
                                          @Nonnull BuiltInAction actionId,
                                          ExecutionContext executionContext) {
        this.accessManager = checkNotNull(accessManager);
        this.userId = checkNotNull(userId);
        this.actionId = checkNotNull(actionId.getActionId());
        this.executionContext = executionContext;
    }

    @Override
    public RequestValidationResult validateAction() {
        if (accessManager.hasPermission(forUser(userId),
                ApplicationResource.get(),
                actionId,
                executionContext)) {
            return RequestValidationResult.getValid();
        } else {
            return RequestValidationResult.getInvalid("You do not have permission for " + actionId.id());
        }
    }
}
