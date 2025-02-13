package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.access.*;
import edu.stanford.protege.webprotege.change.*;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectChangeHandler;
import edu.stanford.protege.webprotege.hierarchy.ordering.ProjectOrderedChildrenManager;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;

import javax.annotation.*;
import jakarta.inject.Inject;

import static edu.stanford.protege.webprotege.access.BuiltInAction.EDIT_ONTOLOGY;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 8 Dec 2017
 */
public class MoveHierarchyNodeActionHandler extends AbstractProjectChangeHandler<Boolean, MoveHierarchyNodeAction, MoveHierarchyNodeResult> {

    private final MoveEntityChangeListGeneratorFactory factory;
    private final ProjectOrderedChildrenManager projectOrderedChildrenManager;

    @Inject
    public MoveHierarchyNodeActionHandler(@Nonnull AccessManager accessManager,

                                          @Nonnull HasApplyChanges applyChanges,
                                          @Nonnull MoveEntityChangeListGeneratorFactory factory,
                                          ProjectOrderedChildrenManager projectOrderedChildrenManager) {
        super(accessManager, applyChanges);
        this.factory = factory;
        this.projectOrderedChildrenManager = projectOrderedChildrenManager;
    }

    @Nonnull
    @Override
    public Class<MoveHierarchyNodeAction> getActionClass() {
        return MoveHierarchyNodeAction.class;
    }

    @Override
    protected ChangeListGenerator<Boolean> getChangeListGenerator(MoveHierarchyNodeAction action, ExecutionContext executionContext) {
        return factory.create(
                action.fromNodePath(),
                action.toNodeParentPath(),
                action.dropType(),
                action.changeRequestId()
        );
    }

    @Override
    protected MoveHierarchyNodeResult createActionResult(ChangeApplicationResult<Boolean> changeApplicationResult,
                                                         MoveHierarchyNodeAction action,
                                                         ExecutionContext executionContext) {
        if (changeApplicationResult.getSubject()) {
            projectOrderedChildrenManager.moveHierarchyNode(action.fromNodePath(), action.toNodeParentPath());
        }
        return new MoveHierarchyNodeResult(changeApplicationResult.getSubject());
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(MoveHierarchyNodeAction action) {
        return EDIT_ONTOLOGY;
    }
}
