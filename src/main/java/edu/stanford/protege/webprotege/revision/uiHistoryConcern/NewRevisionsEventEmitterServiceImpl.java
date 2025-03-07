package edu.stanford.protege.webprotege.revision.uiHistoryConcern;

import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.common.EventId;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.hierarchy.ordering.ProjectOrderedChildren;
import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import edu.stanford.protege.webprotege.ipc.EventDispatcher;
import edu.stanford.protege.webprotege.revision.ProjectChangesManager;
import edu.stanford.protege.webprotege.revision.Revision;
import org.semanticweb.owlapi.model.IRI;

import javax.inject.Inject;
import java.util.Optional;
import java.util.Set;

@ProjectSingleton
public class NewRevisionsEventEmitterServiceImpl implements NewRevisionsEventEmitterService {

    private final ProjectChangesManager changesManager;
    private final OrderingChangesManager orderingChangesManager;
    private final EventDispatcher eventDispatcher;

    private final ProjectId projectId;


    @Inject
    public NewRevisionsEventEmitterServiceImpl(ProjectChangesManager changesManager,
                                               OrderingChangesManager orderingChangesManager,
                                               EventDispatcher eventDispatcher,
                                               ProjectId projectId) {
        this.changesManager = changesManager;
        this.orderingChangesManager = orderingChangesManager;
        this.eventDispatcher = eventDispatcher;
        this.projectId = projectId;
    }


    @Override
    public void emitNewRevisionsEvent(Optional<Revision> revision, ChangeRequestId changeRequestId) {
        revision.ifPresent(rev -> {
            Set<ProjectChangeForEntity> changes = changesManager.getProjectChangesForEntitiesFromRevision(rev);
            //Based on axioms in the revision we can determine if the entity was added/deleted or updated
            //e.g. for adding a new entity you have Declaration axiom
            //
            NewRevisionsEvent revisionsEvent = NewRevisionsEvent.create(EventId.generate(), projectId, changes, changeRequestId);
            eventDispatcher.dispatchEvent(revisionsEvent);
        });
    }

    @Override
    public void emitNewProjectOrderedChildrenEvent(IRI entityParentIri,
                                                   Optional<ProjectOrderedChildren> initialOrderedChildrenOptional,
                                                   Optional<ProjectOrderedChildren> newOrderedChildrenOptional,
                                                   UserId userId,
                                                   ChangeRequestId changeRequestId) {
        newOrderedChildrenOptional.ifPresent(newOrdering -> {
            Set<ProjectChangeForEntity> changes = orderingChangesManager.getProjectChangesForEntitiesFromOrderingChange(entityParentIri, initialOrderedChildrenOptional, newOrdering, userId);

            NewRevisionsEvent revisionsEvent = NewRevisionsEvent.create(EventId.generate(), projectId, changes, changeRequestId);
            eventDispatcher.dispatchEvent(revisionsEvent);
        });
    }
}
