package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.ApplicationActionHandler;
import edu.stanford.protege.webprotege.dispatch.RequestContext;
import edu.stanford.protege.webprotege.dispatch.RequestValidator;
import edu.stanford.protege.webprotege.dispatch.validators.NullValidator;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/04/2013
 */
public class MoveProjectToTrashActionHandler implements ApplicationActionHandler<MoveProjectToTrashAction, MoveProjectToTrashResult> {

    private final ProjectDetailsManager projectDetailsManager;

    @Inject
    public MoveProjectToTrashActionHandler(ProjectDetailsManager projectDetailsManager) {
        this.projectDetailsManager = projectDetailsManager;
    }

    @Nonnull
    @Override
    public Class<MoveProjectToTrashAction> getActionClass() {
        return MoveProjectToTrashAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull MoveProjectToTrashAction action, @Nonnull RequestContext requestContext) {
        return NullValidator.get();
    }

    @Nonnull
    @Override
    public MoveProjectToTrashResult execute(@Nonnull MoveProjectToTrashAction action, @Nonnull ExecutionContext executionContext) {
        ProjectId projectId = action.getProjectId();
            projectDetailsManager.setInTrash(projectId, true);
        return MoveProjectToTrashResult.create();
    }
}
