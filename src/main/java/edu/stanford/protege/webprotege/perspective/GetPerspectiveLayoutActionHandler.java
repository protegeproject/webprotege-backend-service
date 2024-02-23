package edu.stanford.protege.webprotege.perspective;

import edu.stanford.protege.webprotege.dispatch.ProjectActionHandler;
import edu.stanford.protege.webprotege.dispatch.RequestContext;
import edu.stanford.protege.webprotege.dispatch.RequestValidator;
import edu.stanford.protege.webprotege.dispatch.validators.NullValidator;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/02/16
 */
public class GetPerspectiveLayoutActionHandler implements ProjectActionHandler<GetPerspectiveLayoutAction, GetPerspectiveLayoutResult> {

    @Nonnull
    private final PerspectivesManager perspectivesManager;

    @Inject
    public GetPerspectiveLayoutActionHandler(@Nonnull PerspectivesManager perspectivesManager) {
        this.perspectivesManager = checkNotNull(perspectivesManager);
    }

    @Nonnull
    @Override
    public Class<GetPerspectiveLayoutAction> getActionClass() {
        return GetPerspectiveLayoutAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull GetPerspectiveLayoutAction action, @Nonnull RequestContext requestContext) {
        return NullValidator.get();
    }

    @Nonnull
    @Override
    public GetPerspectiveLayoutResult execute(@Nonnull GetPerspectiveLayoutAction action, @Nonnull ExecutionContext executionContext) {
        var perspectiveId = action.perspectiveId();
        var projectId = action.projectId();
        var userId = action.userId();
        var projectPerspective = perspectivesManager.getPerspectiveLayout(projectId, userId, perspectiveId);
        return new GetPerspectiveLayoutResult(projectPerspective);
    }
}
