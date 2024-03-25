package edu.stanford.protege.webprotege.perspective;

import edu.stanford.protege.webprotege.dispatch.ApplicationActionHandler;
import edu.stanford.protege.webprotege.dispatch.RequestContext;
import edu.stanford.protege.webprotege.dispatch.RequestValidator;
import edu.stanford.protege.webprotege.dispatch.validators.NullValidator;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18/02/16
 */
public class GetPerspectivesActionHandler implements ApplicationActionHandler<GetPerspectivesAction, GetPerspectivesResult> {

    private final PerspectivesManager perspectivesManager;

    @Inject
    public GetPerspectivesActionHandler(PerspectivesManager perspectivesManager) {
        this.perspectivesManager = perspectivesManager;
    }

    @Nonnull
    @Override
    public Class<GetPerspectivesAction> getActionClass() {
        return GetPerspectivesAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull GetPerspectivesAction action, @Nonnull RequestContext requestContext) {
        return NullValidator.get();
    }

    @Nonnull
    @Override
    public GetPerspectivesResult execute(@Nonnull GetPerspectivesAction action, @Nonnull ExecutionContext executionContext) {
        var projectId = action.projectId();
        var userId = action.userId();
        var perspectives = perspectivesManager.getPerspectives(projectId, userId);
        var resettablePerspectiveIds = perspectivesManager.getResettablePerspectiveIds(projectId, userId);
        return new GetPerspectivesResult(perspectives, resettablePerspectiveIds);
    }
}
