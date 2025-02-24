package edu.stanford.protege.webprotege.dispatch.handlers;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.dispatch.actions.SaveEntityChildrenOrderingAction;
import edu.stanford.protege.webprotege.dispatch.actions.SaveEntityChildrenOrderingResult;
import edu.stanford.protege.webprotege.hierarchy.ordering.ProjectOrderedChildrenService;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import jakarta.inject.Inject;
import org.jetbrains.annotations.NotNull;



public class SaveEntityChildrenOrderingActionHandler  extends AbstractProjectActionHandler<SaveEntityChildrenOrderingAction, SaveEntityChildrenOrderingResult> {

    private final ProjectOrderedChildrenService service;

    @Inject
    public SaveEntityChildrenOrderingActionHandler(@NotNull AccessManager accessManager,
                                                   ProjectOrderedChildrenService service) {
        super(accessManager);
        this.service = service;
    }

    @NotNull
    @Override
    public Class<SaveEntityChildrenOrderingAction> getActionClass() {
        return SaveEntityChildrenOrderingAction.class;
    }

    @NotNull
    @Override
    public SaveEntityChildrenOrderingResult execute(@NotNull SaveEntityChildrenOrderingAction action, @NotNull ExecutionContext executionContext) {

        service.updateEntity(action, executionContext.userId());

        return new SaveEntityChildrenOrderingResult();
    }
}
