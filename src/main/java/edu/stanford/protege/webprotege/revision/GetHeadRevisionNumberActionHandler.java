package edu.stanford.protege.webprotege.revision;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static edu.stanford.protege.webprotege.access.BuiltInAction.VIEW_CHANGES;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
public class GetHeadRevisionNumberActionHandler extends AbstractProjectActionHandler<GetHeadRevisionNumberAction, GetHeadRevisionNumberResult> {

    @Nonnull
    private final RevisionManager revisionManager;

    @Inject
    public GetHeadRevisionNumberActionHandler(@Nonnull AccessManager accessManager,
                                              @Nonnull RevisionManager revisionManager) {
        super(accessManager);
        this.revisionManager = revisionManager;
    }

    @Nonnull
    @Override
    public Class<GetHeadRevisionNumberAction> getActionClass() {
        return GetHeadRevisionNumberAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetHeadRevisionNumberAction action) {
        return VIEW_CHANGES;
    }

    @Nonnull
    @Override
    public GetHeadRevisionNumberResult execute(@Nonnull GetHeadRevisionNumberAction action, @Nonnull ExecutionContext executionContext) {
        return new GetHeadRevisionNumberResult(action.projectId(), revisionManager.getCurrentRevision());
    }
}
