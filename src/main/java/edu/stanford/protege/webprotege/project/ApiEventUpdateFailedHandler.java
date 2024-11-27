package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.change.RevertRevisionAction;
import edu.stanford.protege.webprotege.change.RevertRevisionActionHandler;
import edu.stanford.protege.webprotege.events.EntityUpdateFailedEvent;
import edu.stanford.protege.webprotege.ipc.EventHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;



@Component
public class ApiEventUpdateFailedHandler implements EventHandler<EntityUpdateFailedEvent> {


    private final ProjectRevisionRepository projectRevisionRepository;

    private final ProjectCache projectCache;


    public ApiEventUpdateFailedHandler(ProjectRevisionRepository projectRevisionRepository, ProjectCache projectCache) {
        this.projectRevisionRepository = projectRevisionRepository;
        this.projectCache = projectCache;
    }


    @NotNull
    @Override
    public String getChannelName() {
        return EntityUpdateFailedEvent.CHANNEL;
    }

    @NotNull
    @Override
    public String getHandlerName() {
        return ApiEventUpdateFailedHandler.class.getName();
    }

    @Override
    public Class<EntityUpdateFailedEvent> getEventClass() {
        return EntityUpdateFailedEvent.class;
    }

    @Override
    public void handleEvent(EntityUpdateFailedEvent event) {
        throw new RuntimeException("Method not supported");
    }

    @Override
    public void handleEvent(EntityUpdateFailedEvent event, ExecutionContext executionContext) {
        List<ProjectRevision> revisions = projectRevisionRepository.findByChangeRequest(event.projectId(), event.changeRequestId());
        RevertRevisionActionHandler revertRevisionActionHandler = (RevertRevisionActionHandler)
                projectCache.getActionHandlerRegistry(event.projectId()).getActionHandler(new RevertRevisionAction(null ,null,null));
        for(ProjectRevision projectRevision : revisions) {
            revertRevisionActionHandler.execute(new RevertRevisionAction(projectRevision.changeRequestId(), projectRevision.projectId(), projectRevision.revisionNumber()), executionContext);
        }
    }
}
