package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.authorization.ProjectResource;
import edu.stanford.protege.webprotege.authorization.Subject;
import edu.stanford.protege.webprotege.dispatch.ApplicationActionHandler;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.dispatch.RequestContext;
import edu.stanford.protege.webprotege.dispatch.RequestValidator;
import edu.stanford.protege.webprotege.dispatch.validators.NullValidator;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import edu.stanford.protege.webprotege.permissions.ProjectPermissionsManager;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.user.UserActivityManager;
import edu.stanford.protege.webprotege.user.UserActivityRecord;
import edu.stanford.protege.webprotege.common.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static edu.stanford.protege.webprotege.access.BuiltInAction.DOWNLOAD_PROJECT;
import static edu.stanford.protege.webprotege.access.BuiltInAction.MOVE_ANY_PROJECT_TO_TRASH;
import static edu.stanford.protege.webprotege.authorization.Subject.forUser;
import static java.util.stream.Collectors.toList;

/**
 * Author: Matthew Horridge<br> Stanford University<br> Bio-Medical Informatics Research Group<br> Date: 01/04/2013
 */
@WebProtegeHandler
public class GetAvailableProjectsActionHandler implements ApplicationActionHandler<GetAvailableProjectsAction, GetAvailableProjectsResult> {

    private final ProjectPermissionsManager projectPermissionsManager;

    private final AccessManager accessManager;

    private final UserActivityManager userActivityManager;

    @Inject
    public GetAvailableProjectsActionHandler(@Nonnull ProjectPermissionsManager projectPermissionsManager,
                                             @Nonnull AccessManager accessManager,
                                             @Nonnull UserActivityManager userActivityManager) {
        this.projectPermissionsManager = projectPermissionsManager;
        this.accessManager = accessManager;
        this.userActivityManager = userActivityManager;
    }

    @Nonnull
    @Override
    public Class<GetAvailableProjectsAction> getActionClass() {
        return GetAvailableProjectsAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull GetAvailableProjectsAction action, @Nonnull RequestContext requestContext) {
        return NullValidator.get();
    }

    @Nonnull
    @Override
    public GetAvailableProjectsResult execute(@Nonnull GetAvailableProjectsAction action, @Nonnull ExecutionContext executionContext) {
        UserId userId = executionContext.getUserId();
        Optional<UserActivityRecord> userActivityRecord = userActivityManager.getUserActivityRecord(executionContext.getUserId());
        Map<ProjectId, Long> lastOpenedMap = new HashMap<>();
        userActivityRecord.ifPresent(record ->
                                             record.getRecentProjects()
                                                   .forEach(recent ->
                                                                    lastOpenedMap.put(recent.getProjectId(), recent.getTimestamp())));
        List<AvailableProject> availableProjects = projectPermissionsManager.getReadableProjects(executionContext).stream()
                                                                            .map(details -> {
                                                                                Subject user = forUser(userId);
                                                                                ProjectResource projectResource = new ProjectResource(details.projectId());
                                                                                boolean downloadable = accessManager.hasPermission(user, projectResource, DOWNLOAD_PROJECT);
                                                                                boolean trashable = details.getOwner().equals(userId)
                                                                                        || accessManager.hasPermission(user, projectResource, MOVE_ANY_PROJECT_TO_TRASH);
                                                                                long lastOpened = lastOpenedMap.getOrDefault(details.projectId(), 0L);
                                                                                return AvailableProject.get(details.projectId(),
                                                                                                            details.getDisplayName(),
                                                                                                            details.getDescription(),
                                                                                                            details.getOwner(),
                                                                                                            details.isInTrash(),
                                                                                                            details.getCreatedAt(),
                                                                                                            details.getCreatedBy(),
                                                                                                            details.getLastModifiedAt(),
                                                                                                            details.getLastModifiedBy(),
                                                                                                            downloadable, trashable, lastOpened);
                                                                            })
                                                                            .collect(toList());
        return new GetAvailableProjectsResult(availableProjects);
    }
}
