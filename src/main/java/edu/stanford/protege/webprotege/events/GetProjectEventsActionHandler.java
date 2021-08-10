package edu.stanford.protege.webprotege.events;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.dispatch.ApplicationActionHandler;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.dispatch.RequestContext;
import edu.stanford.protege.webprotege.dispatch.RequestValidator;
import edu.stanford.protege.webprotege.dispatch.validators.NullValidator;
import edu.stanford.protege.webprotege.event.EventList;
import edu.stanford.protege.webprotege.event.EventTag;
import edu.stanford.protege.webprotege.event.GetProjectEventsAction;
import edu.stanford.protege.webprotege.event.GetProjectEventsResult;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.project.ProjectManager;
import edu.stanford.protege.webprotege.common.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.protege.webprotege.access.BuiltInAction.VIEW_PROJECT;
import static edu.stanford.protege.webprotege.authorization.api.ProjectResource.forProject;
import static edu.stanford.protege.webprotege.authorization.api.Subject.forUser;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/03/2013
 */
public class GetProjectEventsActionHandler implements ApplicationActionHandler<GetProjectEventsAction, GetProjectEventsResult> {

    @Nonnull
    private final ProjectManager projectManager;

    @Nonnull
    private final AccessManager accessManager;

    @Inject
    public GetProjectEventsActionHandler(@Nonnull ProjectManager projectManager,
                                         @Nonnull AccessManager accessManager) {
        this.projectManager = checkNotNull(projectManager);
        this.accessManager = checkNotNull(accessManager);
    }

    @Nonnull
    @Override
    public Class<GetProjectEventsAction> getActionClass() {
        return GetProjectEventsAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull GetProjectEventsAction action, @Nonnull RequestContext requestContext) {
        return NullValidator.get();
    }

    @Nonnull
    @Override
    public GetProjectEventsResult execute(@Nonnull GetProjectEventsAction action, @Nonnull ExecutionContext executionContext) {
        EventTag sinceTag = action.getSinceTag();
        ProjectId projectId = action.getProjectId();
        UserId userId = executionContext.getUserId();
        if(!accessManager.hasPermission(forUser(userId),
                                        forProject(action.getProjectId()),
                                        VIEW_PROJECT)) {
            return getEmptyResult(projectId, sinceTag);
        }
        EventList<?> projectEventList = projectManager.getProjectEventsSinceTag(projectId, sinceTag);
        return  GetProjectEventsResult.create(projectEventList);
    }

    private static GetProjectEventsResult getEmptyResult(ProjectId projectId, EventTag sinceTag) {
        return GetProjectEventsResult.create(EventList.create(sinceTag, ImmutableList.of(), sinceTag));
    }
}
