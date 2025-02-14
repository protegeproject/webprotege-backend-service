package edu.stanford.protege.webprotege.hierarchy.ordering;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.locking.ReadWriteLockService;
import org.semanticweb.owlapi.model.*;

import javax.inject.Inject;
import java.util.*;

public class ProjectOrderedChildrenManager {

    private final ProjectId projectId;
    private final ProjectOrderedChildrenServiceImpl projectOrderedChildrenService;

    private final ReadWriteLockService readWriteLockService;


    @Inject
    public ProjectOrderedChildrenManager(ProjectId projectId, ProjectOrderedChildrenServiceImpl projectOrderedChildrenService,
                                         ReadWriteLockService readWriteLockService) {
        this.projectId = projectId;
        this.projectOrderedChildrenService = projectOrderedChildrenService;
        this.readWriteLockService = readWriteLockService;
    }

    public void moveHierarchyNode(OWLEntity fromEntity, OWLEntity toEntity, OWLEntity childEntity) {
        String entityUri = childEntity.toStringID();
        String fromParentUri = fromEntity.toStringID();
        String toParentUri = toEntity.toStringID();

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
