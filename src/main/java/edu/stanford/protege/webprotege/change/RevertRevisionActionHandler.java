package edu.stanford.protege.webprotege.change;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectChangeHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.revision.RevisionNumber;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/03/15
 */
public class RevertRevisionActionHandler extends AbstractProjectChangeHandler<Boolean, RevertRevisionAction, RevertRevisionResult> {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final RevisionReverterChangeListGeneratorFactory factory;

    @Inject
    public RevertRevisionActionHandler(@Nonnull AccessManager accessManager,

                                       @Nonnull HasApplyChanges applyChanges,
                                       @Nonnull ProjectId projectId,
                                       @Nonnull RevisionReverterChangeListGeneratorFactory factory) {
        super(accessManager, applyChanges);
        this.projectId = projectId;
        this.factory = factory;
    }

    @Nonnull
    @Override
    public Class<RevertRevisionAction> getActionClass() {
        return RevertRevisionAction.class;
    }

    @Override
    protected ChangeListGenerator<Boolean> getChangeListGenerator(RevertRevisionAction action,
                                                                    ExecutionContext executionContext) {
        RevisionNumber revisionNumber = action.revisionNumber();
        return factory.create(revisionNumber, action.changeRequestId());
    }

    @Override
    protected RevertRevisionResult createActionResult(ChangeApplicationResult<Boolean> changeApplicationResult,
                                                      RevertRevisionAction action,
                                                      ExecutionContext executionContext) {
        return new RevertRevisionResult(projectId, action.revisionNumber());
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(RevertRevisionAction action) {
        return BuiltInAction.REVERT_CHANGES;
    }
}
