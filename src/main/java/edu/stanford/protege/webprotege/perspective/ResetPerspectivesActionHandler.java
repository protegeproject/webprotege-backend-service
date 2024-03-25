package edu.stanford.protege.webprotege.perspective;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-03
 */
public class ResetPerspectivesActionHandler extends AbstractProjectActionHandler<ResetPerspectivesAction, ResetPerspectivesResult> {

    private final PerspectivesManager perspectivesManager;

    @Inject
    public ResetPerspectivesActionHandler(@Nonnull AccessManager accessManager, PerspectivesManager perspectivesManager) {
        super(accessManager);
        this.perspectivesManager = checkNotNull(perspectivesManager);
    }

    @Nonnull
    @Override
    public Class<ResetPerspectivesAction> getActionClass() {
        return ResetPerspectivesAction.class;
    }

    @Nonnull
    @Override
    public ResetPerspectivesResult execute(@Nonnull ResetPerspectivesAction action,
                                           @Nonnull ExecutionContext executionContext) {
        perspectivesManager.resetPerspectives(action.projectId(),
                                              executionContext.userId());
        return new ResetPerspectivesResult();
    }
}
