package edu.stanford.protege.webprotege.perspective;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15 Mar 2017
 */
public class ResetPerspectiveLayoutActionHandler extends AbstractProjectActionHandler<ResetPerspectiveLayoutAction, ResetPerspectiveLayoutResult> {

    private final PerspectivesManager perspectivesManager;

    @Inject
    public ResetPerspectiveLayoutActionHandler(@Nonnull AccessManager accessManager,
                                               PerspectivesManager perspectivesManager) {
        super(accessManager);
        this.perspectivesManager = checkNotNull(perspectivesManager);
    }

    @Nonnull
    @Override
    public Class<ResetPerspectiveLayoutAction> getActionClass() {
        return ResetPerspectiveLayoutAction.class;
    }

    @Nonnull
    @Override
    public ResetPerspectiveLayoutResult execute(@Nonnull ResetPerspectiveLayoutAction action,
                                                @Nonnull ExecutionContext executionContext) {
        var projectId = action.getProjectId();
        var perspectiveId = action.getPerspectiveId();
        var userId = executionContext.getUserId();
        perspectivesManager.resetPerspectiveLayout(projectId, userId, perspectiveId);
        var perspectiveLayout = perspectivesManager.getPerspectiveLayout(projectId, userId, perspectiveId);
        return ResetPerspectiveLayoutResult.create(perspectiveLayout);
    }
}
