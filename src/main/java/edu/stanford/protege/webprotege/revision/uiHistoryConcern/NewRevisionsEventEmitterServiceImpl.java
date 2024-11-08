package edu.stanford.protege.webprotege.revision.uiHistoryConcern;

import edu.stanford.protege.webprotege.common.*;
import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import edu.stanford.protege.webprotege.ipc.EventDispatcher;
import edu.stanford.protege.webprotege.revision.*;

import javax.inject.Inject;
import java.util.*;

@ProjectSingleton
public class NewRevisionsEventEmitterServiceImpl implements NewRevisionsEventEmitterService {

    private final ProjectChangesManager changesManager;
    private final EventDispatcher eventDispatcher;

    private final ProjectId projectId;


    @Inject
    public NewRevisionsEventEmitterServiceImpl(ProjectChangesManager changesManager, EventDispatcher eventDispatcher, ProjectId projectId) {
        this.changesManager = changesManager;
        this.eventDispatcher = eventDispatcher;
        this.projectId = projectId;
    }


    @Override
    public void emitNewRevisionsEvent(Optional<Revision> revision) {
        revision.ifPresent(rev -> {
            Set<ProjectChangeForEntity> changeList = changesManager.getProjectChangesForEntitiesFromRevision(rev);
            //Based on axioms in the revision we can determine if the entity was added/deleted or updated
            //e.g. for adding a new entity you have Declaration axiom
            //
            NewRevisionsEvent revisionsEvent = NewRevisionsEvent.create(EventId.generate(), projectId, changeList);
            eventDispatcher.dispatchEvent(revisionsEvent);
        });
    }
}
