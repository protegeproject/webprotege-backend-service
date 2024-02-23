package edu.stanford.protege.webprotege.permissions;

import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.authorization.ActionId;
import edu.stanford.protege.webprotege.dispatch.ApplicationActionHandler;
import edu.stanford.protege.webprotege.dispatch.RequestContext;
import edu.stanford.protege.webprotege.dispatch.RequestValidator;
import edu.stanford.protege.webprotege.dispatch.validators.NullValidator;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.protege.webprotege.authorization.ProjectResource.forProject;
import static edu.stanford.protege.webprotege.authorization.Subject.forUser;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23/02/15
 */
public class GetProjectPermissionsActionHandler implements ApplicationActionHandler<GetProjectPermissionsAction, GetProjectPermissionsResult> {

    @Nonnull
    private final AccessManager accessManager;

    @Inject
    public GetProjectPermissionsActionHandler(@Nonnull AccessManager accessManager) {
        this.accessManager = checkNotNull(accessManager);
    }

    @Nonnull
    @Override
    public Class<GetProjectPermissionsAction> getActionClass() {
        return GetProjectPermissionsAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull GetProjectPermissionsAction action, @Nonnull RequestContext requestContext) {
        return NullValidator.get();
    }

    @Nonnull
    @Override
    public GetProjectPermissionsResult execute(@Nonnull GetProjectPermissionsAction action, @Nonnull ExecutionContext executionContext) {
        Set<ActionId> allowedActions = accessManager.getActionClosure(
                forUser(executionContext.userId()),
                forProject(action.projectId()),
                new edu.stanford.protege.webprotege.ipc.ExecutionContext(executionContext.userId(), executionContext.jwt()));
        return new GetProjectPermissionsResult(ImmutableSet.copyOf(allowedActions));
    }
}
