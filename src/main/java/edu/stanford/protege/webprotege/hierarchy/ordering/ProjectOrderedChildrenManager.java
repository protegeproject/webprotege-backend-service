package edu.stanford.protege.webprotege.hierarchy.ordering;

import edu.stanford.protege.webprotege.bulkop.MoveEntitiesToParentAction;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.hierarchy.*;
import edu.stanford.protege.webprotege.locking.ReadWriteLockService;

import javax.inject.Inject;
import java.util.*;

public class ProjectOrderedChildrenManager {

    private final ProjectOrderedChildrenServiceImpl projectOrderedChildrenService;

    private final ReadWriteLockService readWriteLockService;

    private final ClassHierarchyProvider classHierarchyProvider;

    @Inject
    public ProjectOrderedChildrenManager(ProjectOrderedChildrenServiceImpl projectOrderedChildrenService,
                                         ReadWriteLockService readWriteLockService,
                                         ClassHierarchyProvider classHierarchyProvider) {
        this.projectOrderedChildrenService = projectOrderedChildrenService;
        this.readWriteLockService = readWriteLockService;
        this.classHierarchyProvider = classHierarchyProvider;
    }

    public void moveHierarchyNode(MoveHierarchyNodeAction action) {
        ProjectId projectId = action.projectId();
        String entityUri = action.fromNodePath().getLast().get().getEntity().toStringID();
        String fromParentUri = action.fromNodePath().getLastPredecessor().get().getEntity().toStringID();
        String toParentUri = action.toNodeParentPath().getLastPredecessor().get().getEntity().toStringID();

        readWriteLockService.executeWriteLock(() -> {
            projectOrderedChildrenService.removeChildFromParent(projectId, fromParentUri, entityUri);

            projectOrderedChildrenService.addChildToParent(projectId, toParentUri, entityUri);
        });
    }

    public void moveEntitiesToParent(MoveEntitiesToParentAction action, Map<String, List<String>> entitiesWithPreviousParents) {
        String newParentIri = action.entity().toStringID();
        readWriteLockService.executeWriteLock(() -> {
            entitiesWithPreviousParents.forEach((childUri, parents) -> {
                parents.forEach(parent -> projectOrderedChildrenService.removeChildFromParent(action.projectId(), parent, childUri));
            });
            action.entities().forEach(entity -> projectOrderedChildrenService.addChildToParent(action.projectId(), newParentIri, entity.toStringID()));
        });
    }
}
