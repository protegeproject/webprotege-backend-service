package edu.stanford.protege.webprotege.project;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.dispatch.ApplicationActionHandler;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.dispatch.RequestContext;
import edu.stanford.protege.webprotege.dispatch.RequestValidator;
import edu.stanford.protege.webprotege.dispatch.validators.NullValidator;
import edu.stanford.protege.webprotege.event.EventList;
import edu.stanford.protege.webprotege.event.EventTag;
import edu.stanford.protege.webprotege.event.ProjectMovedToTrashEvent;
import edu.stanford.protege.webprotege.event.WebProtegeEvent;
import edu.stanford.protege.webprotege.common.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/04/2013
 */
public class MoveProjectToTrashActionHandler implements ApplicationActionHandler<MoveProjectToTrashAction, MoveProjectToTrashResult> {

    private ProjectDetailsManager projectDetailsManager;

    @Inject
    public MoveProjectToTrashActionHandler(ProjectDetailsManager projectDetailsManager) {
        this.projectDetailsManager = projectDetailsManager;
    }

    @Nonnull
    @Override
    public Class<MoveProjectToTrashAction> getActionClass() {
        return MoveProjectToTrashAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull MoveProjectToTrashAction action, @Nonnull RequestContext requestContext) {
        return NullValidator.get();
    }

    @Nonnull
    @Override
    public MoveProjectToTrashResult execute(@Nonnull MoveProjectToTrashAction action, @Nonnull ExecutionContext executionContext) {
        List<ProjectMovedToTrashEvent> events = new ArrayList<>();
        ProjectId projectId = action.getProjectId();
            projectDetailsManager.setInTrash(projectId, true);
            events.add(new ProjectMovedToTrashEvent(projectId));

        EventList<WebProtegeEvent<?>> eventList = EventList.create(EventTag.getFirst(),
                                                                ImmutableList.copyOf(events),
                                                                EventTag.getFirst());
        return MoveProjectToTrashResult.create();
    }
}
