package edu.stanford.protege.webprotege.change;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.common.Page;
import edu.stanford.protege.webprotege.revision.ProjectChangesManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static edu.stanford.protege.webprotege.access.BuiltInAction.VIEW_CHANGES;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24/02/15
 */
public class GetProjectChangesActionHandler extends AbstractProjectActionHandler<GetProjectChangesAction, GetProjectChangesResult> {

    @Nonnull
    private final ProjectChangesManager changesManager;

    @Inject
    public GetProjectChangesActionHandler(@Nonnull AccessManager accessManager,
                                          @Nonnull ProjectChangesManager changesManager) {
        super(accessManager);
        this.changesManager = changesManager;
    }

    @Nonnull
    @Override
    public Class<GetProjectChangesAction> getActionClass() {
        return GetProjectChangesAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetProjectChangesAction action) {
        return VIEW_CHANGES;
    }

    @Nonnull
    @Override
    public GetProjectChangesResult execute(@Nonnull final GetProjectChangesAction action, @Nonnull ExecutionContext executionContext) {
        Page<ProjectChange> changeList = changesManager.getProjectChanges(action.subject(),
                                                                          action.pageRequest());
        return new GetProjectChangesResult(changeList);
    }
}
