package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.authorization.api.ProjectResource;
import edu.stanford.protege.webprotege.authorization.api.Subject;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import edu.stanford.protege.webprotege.permissions.ProjectPermissionsManager;
import edu.stanford.protege.webprotege.user.UserActivityManager;
import edu.stanford.protege.webprotege.user.UserActivityRecord;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

import java.util.*;

import static edu.stanford.protege.webprotege.access.BuiltInAction.DOWNLOAD_PROJECT;
import static edu.stanford.protege.webprotege.access.BuiltInAction.MOVE_ANY_PROJECT_TO_TRASH;
import static edu.stanford.protege.webprotege.authorization.api.Subject.forUser;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-08-19
 */
@WebProtegeHandler
public class GetAvailableProjectsHandler implements CommandHandler<GetAvailableProjectsAction, GetAvailableProjectsResult> {


    private final GetAvailableProjectsActionHandler delegate;

    private UserActivityManager userActivityManager;

    private ProjectPermissionsManager projectPermissionsManager;

    private AccessManager accessManager;

    public GetAvailableProjectsHandler(GetAvailableProjectsActionHandler delegate) {
        this.delegate = Objects.requireNonNull(delegate);
    }

    @NotNull
    @Override
    public String getChannelName() {
        return "projects.GetAvailableProjects";
    }

    @Override
    public Class<GetAvailableProjectsAction> getRequestClass() {
        return GetAvailableProjectsAction.class;
    }

    @Override
    public Mono<GetAvailableProjectsResult> handleRequest(GetAvailableProjectsAction request,
                                                          ExecutionContext executionContext) {
        UserId userId = executionContext.userId();
        Optional<UserActivityRecord> userActivityRecord = userActivityManager.getUserActivityRecord(userId);
        Map<ProjectId, Long> lastOpenedMap = new HashMap<>();
        userActivityRecord.ifPresent(record -> record.getRecentProjects()
                                                     .forEach(recent -> lastOpenedMap.put(recent.getProjectId(),
                                                                                          recent.getTimestamp())));
        List<AvailableProject> availableProjects = projectPermissionsManager.getReadableProjects(userId)
                                                                            .stream()
                                                                            .map(details -> {
                                                                                Subject user = forUser(userId);
                                                                                ProjectResource projectResource = new ProjectResource(
                                                                                        details.getProjectId());
                                                                                boolean downloadable = accessManager.hasPermission(
                                                                                        user,
                                                                                        projectResource,
                                                                                        DOWNLOAD_PROJECT);
                                                                                boolean trashable = details.getOwner()
                                                                                                           .equals(userId) || accessManager.hasPermission(
                                                                                        user,
                                                                                        projectResource,
                                                                                        MOVE_ANY_PROJECT_TO_TRASH);
                                                                                long lastOpened = lastOpenedMap.getOrDefault(
                                                                                        details.getProjectId(),
                                                                                        0L);
                                                                                return AvailableProject.get(details.getProjectId(),
                                                                                                            details.getDisplayName(),
                                                                                                            details.getDescription(),
                                                                                                            details.getOwner(),
                                                                                                            details.isInTrash(),
                                                                                                            details.getCreatedAt(),
                                                                                                            details.getCreatedBy(),
                                                                                                            details.getLastModifiedAt(),
                                                                                                            details.getLastModifiedBy(),
                                                                                                            downloadable,
                                                                                                            trashable,
                                                                                                            lastOpened);
                                                                            })
                                                                            .collect(toList());
        return Mono.just(GetAvailableProjectsResult.create(availableProjects));
    }
}
