package edu.stanford.protege.webprotege.hierarchy.ordering;

import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import edu.stanford.protege.webprotege.locking.ReadWriteLockService;
import edu.stanford.protege.webprotege.revision.uiHistoryConcern.NewRevisionsEventEmitterService;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import jakarta.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@ProjectSingleton
public class ProjectOrderedChildrenManager {

    private final ProjectId projectId;
    private final ProjectOrderedChildrenService projectOrderedChildrenService;

    private final ReadWriteLockService readWriteLockService;

    private final NewRevisionsEventEmitterService newRevisionsEventEmitterService;


    @Inject
    public ProjectOrderedChildrenManager(ProjectId projectId,
                                         ProjectOrderedChildrenService projectOrderedChildrenService,
                                         ReadWriteLockService readWriteLockService,
                                         NewRevisionsEventEmitterService newRevisionsEventEmitterService) {
        this.projectId = projectId;
        this.projectOrderedChildrenService = projectOrderedChildrenService;
        this.readWriteLockService = readWriteLockService;
        this.newRevisionsEventEmitterService = newRevisionsEventEmitterService;
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

    public void changeEntityParents(IRI entity, Set<IRI> removedParents, Set<IRI> newParents) {
        removedParents.forEach(removedParent -> projectOrderedChildrenService.removeChildFromParent(projectId, removedParent.toString(), entity.toString()));
        newParents.forEach(newParent -> projectOrderedChildrenService.addChildToParent(projectId, newParent.toString(), entity.toString()));
    }

    public void updateChildrenOrderingForEntity(IRI entityParentIri,
                                                List<String> newChildrenOrder,
                                                UserId userId,
                                                ChangeRequestId changeRequestId) {
        readWriteLockService.executeWriteLock(() -> {

            Optional<ProjectOrderedChildren> initialOrderedChildrenOptional = projectOrderedChildrenService.findOrderedChildren(projectId, entityParentIri);

            Optional<ProjectOrderedChildren> newOrderedChildrenOptional = projectOrderedChildrenService.updateEntityAndGet(entityParentIri,
                    projectId,
                    newChildrenOrder,
                    initialOrderedChildrenOptional,
                    userId);

            newRevisionsEventEmitterService.emitNewProjectOrderedChildrenEvent(
                    entityParentIri,
                    initialOrderedChildrenOptional,
                    newOrderedChildrenOptional,
                    userId,
                    changeRequestId
            );
        });
    }
}
