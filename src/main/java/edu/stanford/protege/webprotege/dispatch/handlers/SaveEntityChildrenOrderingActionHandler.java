package edu.stanford.protege.webprotege.dispatch.handlers;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.dispatch.actions.SaveEntityChildrenOrderingAction;
import edu.stanford.protege.webprotege.dispatch.actions.SaveEntityChildrenOrderingResult;
import edu.stanford.protege.webprotege.hierarchy.ordering.EntityChildrenOrdering;
import edu.stanford.protege.webprotege.hierarchy.ordering.ProjectOrderedChildrenRepository;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

public class SaveEntityChildrenOrderingActionHandler  extends AbstractProjectActionHandler<SaveEntityChildrenOrderingAction, SaveEntityChildrenOrderingResult> {

    private final ProjectOrderedChildrenRepository repository;

    @Inject
    public SaveEntityChildrenOrderingActionHandler(@NotNull AccessManager accessManager,
                                                   ProjectOrderedChildrenRepository repository) {
        super(accessManager);
        this.repository = repository;
    }

    @NotNull
    @Override
    public Class<SaveEntityChildrenOrderingAction> getActionClass() {
        return SaveEntityChildrenOrderingAction.class;
    }

    @NotNull
    @Override
    public SaveEntityChildrenOrderingResult execute(@NotNull SaveEntityChildrenOrderingAction action, @NotNull ExecutionContext executionContext) {
        EntityChildrenOrdering entityChildrenOrdering = repository.findOrderedChildren(action.projectId(), action.entityIri().toString());

        if(entityChildrenOrdering == null) {
            entityChildrenOrdering = new EntityChildrenOrdering(action.entityIri().toString(),
                    action.projectId(),
                    action.orderedChildren(),
                    executionContext.userId().id());
        } else {
            entityChildrenOrdering = new EntityChildrenOrdering(entityChildrenOrdering.entityUri(),
                    entityChildrenOrdering.projectId(),
                    action.orderedChildren(),
                    entityChildrenOrdering.userId());
        }

        repository.saveOrUpdate(entityChildrenOrdering);

        return new SaveEntityChildrenOrderingResult();
    }
}
