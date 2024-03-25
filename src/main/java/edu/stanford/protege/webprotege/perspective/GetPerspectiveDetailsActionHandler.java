package edu.stanford.protege.webprotege.perspective;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-03
 */
public class GetPerspectiveDetailsActionHandler extends AbstractProjectActionHandler<GetPerspectiveDetailsAction, GetPerspectiveDetailsResult> {

    private final PerspectivesManager perspectivesManager;

    @Inject
    public GetPerspectiveDetailsActionHandler(@Nonnull AccessManager accessManager,
                                              PerspectivesManager perspectivesManager) {
        super(accessManager);
        this.perspectivesManager = checkNotNull(perspectivesManager);
    }

    @Nonnull
    @Override
    public Class<GetPerspectiveDetailsAction> getActionClass() {
        return GetPerspectiveDetailsAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetPerspectiveDetailsAction action) {
        return BuiltInAction.VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public GetPerspectiveDetailsResult execute(@Nonnull GetPerspectiveDetailsAction action,
                                               @Nonnull ExecutionContext executionContext) {
        var projectId = action.projectId();
        var userId = executionContext.userId();
        var perspectiveDetails = perspectivesManager.getPerspectiveDetails(projectId, userId);
        return new GetPerspectiveDetailsResult(perspectiveDetails);
    }
}
