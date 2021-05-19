package edu.stanford.protege.webprotege.dispatch.handlers;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.dispatch.ApplicationActionHandler;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.dispatch.RequestContext;
import edu.stanford.protege.webprotege.dispatch.RequestValidator;
import edu.stanford.protege.webprotege.dispatch.validators.UserIsProjectOwnerValidator;
import edu.stanford.protege.webprotege.project.ProjectDetailsManager;
import edu.stanford.protege.webprotege.event.EventList;
import edu.stanford.protege.webprotege.event.EventTag;
import edu.stanford.protege.webprotege.event.ProjectMovedFromTrashEvent;
import edu.stanford.protege.webprotege.event.WebProtegeEvent;
import edu.stanford.protege.webprotege.project.ProjectId;
import edu.stanford.protege.webprotege.project.RemoveProjectFromTrashAction;
import edu.stanford.protege.webprotege.project.RemoveProjectFromTrashResult;

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
