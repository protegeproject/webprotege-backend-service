package edu.stanford.protege.webprotege.logicaldefinitions;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.change.ChangeApplicationResult;
import edu.stanford.protege.webprotege.change.ChangeListGenerator;
import edu.stanford.protege.webprotege.change.HasApplyChanges;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectChangeHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class UpdateLogicalDefinitionsActionHandler extends AbstractProjectChangeHandler<Boolean, UpdateLogicalDefinitionsAction, UpdateLogicalDefinitionsResponse> {

    @Nonnull
    private final UpdateLogicalDefinitionsChangeListGeneratorFactory factory;

    public UpdateLogicalDefinitionsActionHandler(
            @NotNull AccessManager accessManager,
            @NotNull HasApplyChanges applyChanges,
            @NotNull UpdateLogicalDefinitionsChangeListGeneratorFactory factory) {
        super(accessManager, applyChanges);
        this.factory = factory;
    }

    @Override
    protected ChangeListGenerator<Boolean> getChangeListGenerator(UpdateLogicalDefinitionsAction action, ExecutionContext executionContext) {
        return factory.create(action.changeRequestId(), action.projectId(), action.subject(),
                action.pristineLogicalConditions(), action.changedLogicalConditions());
    }

    @Override
    protected UpdateLogicalDefinitionsResponse createActionResult(ChangeApplicationResult<Boolean> changeApplicationResult, UpdateLogicalDefinitionsAction action, ExecutionContext executionContext) {
        return new UpdateLogicalDefinitionsResponse();
    }

    @NotNull
    @Override
    public Class<UpdateLogicalDefinitionsAction> getActionClass() {
        return UpdateLogicalDefinitionsAction.class;
    }
}
