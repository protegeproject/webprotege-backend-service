package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.ApplicationActionHandler;
import edu.stanford.protege.webprotege.dispatch.RequestContext;
import edu.stanford.protege.webprotege.dispatch.RequestValidator;
import edu.stanford.protege.webprotege.dispatch.validators.UserIsProjectOwnerValidator;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/04/2013
 */
public class RemoveProjectFromTrashActionHandler implements ApplicationActionHandler<RemoveProjectFromTrashAction, RemoveProjectFromTrashResult> {

    private final ProjectDetailsManager projectDetailsManager;

    @Inject
    public RemoveProjectFromTrashActionHandler(ProjectDetailsManager projectDetailsManager) {
        this.projectDetailsManager = projectDetailsManager;
    }

    @Nonnull
    @Override
    public Class<RemoveProjectFromTrashAction> getActionClass() {
        return RemoveProjectFromTrashAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull RemoveProjectFromTrashAction action, @Nonnull RequestContext requestContext) {
        return new UserIsProjectOwnerValidator(action.getProjectId(),
                                               requestContext.getUserId(),
                                               projectDetailsManager);
    }

    @Nonnull
    @Override
    public RemoveProjectFromTrashResult execute(@Nonnull RemoveProjectFromTrashAction action, @Nonnull ExecutionContext executionContext) {
        ProjectId projectId = action.getProjectId();
        projectDetailsManager.setInTrash(projectId, false);
        return RemoveProjectFromTrashResult.create();
    }
}
