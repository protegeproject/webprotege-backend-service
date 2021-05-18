package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.dispatch.ApplicationActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserIsProjectOwnerValidator;
import edu.stanford.bmir.protege.web.server.project.ProjectDetailsManager;
import edu.stanford.bmir.protege.web.server.event.EventList;
import edu.stanford.bmir.protege.web.server.event.EventTag;
import edu.stanford.bmir.protege.web.server.event.ProjectMovedFromTrashEvent;
import edu.stanford.bmir.protege.web.server.event.WebProtegeEvent;
import edu.stanford.bmir.protege.web.server.project.ProjectId;
import edu.stanford.bmir.protege.web.server.project.RemoveProjectFromTrashAction;
import edu.stanford.bmir.protege.web.server.project.RemoveProjectFromTrashResult;

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
public class RemoveProjectsFromTrashActionHandler implements ApplicationActionHandler<RemoveProjectFromTrashAction, RemoveProjectFromTrashResult> {

    private final ProjectDetailsManager projectDetailsManager;

    @Inject
    public RemoveProjectsFromTrashActionHandler(ProjectDetailsManager projectDetailsManager) {
        this.projectDetailsManager = projectDetailsManager;
    }

    @Nonnull
    @Override
    public Class<RemoveProjectFromTrashAction> getActionClass() {
        return RemoveProjectFromTrashAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull RemoveProjectFromTrashAction action, @Nonnull RequestContext requestContext) {
        return new UserIsProjectOwnerValidator(action.getProjectId(),
                                               requestContext.getUserId(),
                                               projectDetailsManager);
    }

    @Nonnull
    @Override
    public RemoveProjectFromTrashResult execute(@Nonnull RemoveProjectFromTrashAction action, @Nonnull ExecutionContext executionContext) {
        List<WebProtegeEvent<?>> events = new ArrayList<>();
        ProjectId projectId = action.getProjectId();
        projectDetailsManager.setInTrash(projectId, false);
        events.add(new ProjectMovedFromTrashEvent(projectId));
        EventList<WebProtegeEvent<?>> eventList = EventList.create(EventTag.getFirst(), ImmutableList.copyOf(events), EventTag.getFirst());
        return RemoveProjectFromTrashResult.create(eventList);
    }
}
