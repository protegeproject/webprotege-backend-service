package edu.stanford.protege.webprotege.hierarchy.ordering;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.entity.EntityNode;
import edu.stanford.protege.webprotege.hierarchy.*;
import edu.stanford.protege.webprotege.locking.ReadWriteLockService;
import org.semanticweb.owlapi.model.OWLClass;

import javax.inject.Inject;
import java.util.*;

public class ProjectOrderedChildrenManager {

    private final ProjectId projectId;
    private final ProjectOrderedChildrenServiceImpl projectOrderedChildrenService;

    private final ReadWriteLockService readWriteLockService;

    private final ClassHierarchyProvider classHierarchyProvider;

    @Inject
    public ProjectOrderedChildrenManager(ProjectId projectId, ProjectOrderedChildrenServiceImpl projectOrderedChildrenService,
                                         ReadWriteLockService readWriteLockService,
                                         ClassHierarchyProvider classHierarchyProvider) {
        this.projectId = projectId;
        this.projectOrderedChildrenService = projectOrderedChildrenService;
        this.readWriteLockService = readWriteLockService;
        this.classHierarchyProvider = classHierarchyProvider;
    }

    public void moveHierarchyNode(Path<EntityNode> fromNodePath, Path<EntityNode> toNodeParentPath) {
        String entityUri = fromNodePath.getLast().get().getEntity().toStringID();
        String fromParentUri = fromNodePath.getLastPredecessor().get().getEntity().toStringID();
        String toParentUri = toNodeParentPath.getLast().get().getEntity().toStringID();

        readWriteLockService.executeWriteLock(() -> {
            projectOrderedChildrenService.removeChildFromParent(projectId, fromParentUri, entityUri);

            projectOrderedChildrenService.addChildToParent(projectId, toParentUri, entityUri);
        });
    }

    public void moveEntitiesToParent(OWLClass newParent, Set<OWLClass> entitiesToBeMoved, Map<String, List<String>> entitiesWithPreviousParents) {
        String newParentIri = newParent.toStringID();
        readWriteLockService.executeWriteLock(() -> {
            entitiesWithPreviousParents.forEach((childUri, parents) -> parents.forEach(parent -> projectOrderedChildrenService.removeChildFromParent(projectId, parent, childUri)));
            entitiesToBeMoved.forEach(entity -> projectOrderedChildrenService.addChildToParent(projectId, newParentIri, entity.toStringID()));
        });
    }
}
