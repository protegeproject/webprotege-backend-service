package edu.stanford.protege.webprotege.perspective;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import jakarta.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18/02/16
 */
public class SetPerspectivesActionHandler extends AbstractProjectActionHandler<SetPerspectivesAction, SetPerspectivesResult> {

    private final PerspectivesManager perspectivesManager;

    @Inject
    public SetPerspectivesActionHandler(@Nonnull AccessManager accessManager,
                                        @Nonnull PerspectivesManager perspectivesManager) {
        super(accessManager);
        this.perspectivesManager = perspectivesManager;
    }

    @Nonnull
    @Override
    public Class<SetPerspectivesAction> getActionClass() {
        return SetPerspectivesAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(SetPerspectivesAction action) {
        if(action.getUserId().isEmpty()) {
            return BuiltInAction.EDIT_PROJECT_SETTINGS;
        }
        else {
            return BuiltInAction.VIEW_PROJECT;
        }
    }

    @Nonnull
    @Override
    public SetPerspectivesResult execute(@Nonnull SetPerspectivesAction action, @Nonnull ExecutionContext executionContext) {
        var projectId = action.projectId();
        var userId = action.getUserId();
        var perspectiveDescriptors = action.perspectiveIds();
        var executingUser = executionContext.userId();
        if(userId.isPresent()) {
            perspectivesManager.setPerspectives(projectId, userId.get(), perspectiveDescriptors);
        }
        else {
            perspectivesManager.savePerspectivesAsProjectDefault(projectId, perspectiveDescriptors, executingUser);
        }
        var resettablePerspectives = perspectivesManager.getResettablePerspectiveIds(projectId, executingUser);
        return new SetPerspectivesResult(perspectiveDescriptors, resettablePerspectives);
    }
}
