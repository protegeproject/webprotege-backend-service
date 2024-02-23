package edu.stanford.protege.webprotege.permissions;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.dispatch.ApplicationActionHandler;
import edu.stanford.protege.webprotege.dispatch.RequestContext;
import edu.stanford.protege.webprotege.dispatch.RequestValidator;
import edu.stanford.protege.webprotege.dispatch.validators.ApplicationPermissionValidator;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static edu.stanford.protege.webprotege.access.BuiltInAction.REBUILD_PERMISSIONS;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Apr 2017
 */
public class RebuildPermissionsActionHandler implements ApplicationActionHandler<RebuildPermissionsAction, RebuildPermissionsResult> {

    private final AccessManager accessManager;

    @Inject
    public RebuildPermissionsActionHandler(AccessManager accessManager) {
        this.accessManager = accessManager;
    }

    @Nonnull
    @Override
    public Class<RebuildPermissionsAction> getActionClass() {
        return RebuildPermissionsAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull RebuildPermissionsAction action, @Nonnull RequestContext requestContext) {
        return new ApplicationPermissionValidator(accessManager, requestContext.getUserId(), REBUILD_PERMISSIONS, requestContext.getExecutionContext());
    }

    @Nonnull
    @Override
    public RebuildPermissionsResult execute(@Nonnull RebuildPermissionsAction action, @Nonnull ExecutionContext executionContext) {
        accessManager.rebuild();
        return new RebuildPermissionsResult();
    }
}
