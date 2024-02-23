package edu.stanford.protege.webprotege.dispatch.handlers;

import com.google.common.base.Stopwatch;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.ApplicationActionHandler;
import edu.stanford.protege.webprotege.dispatch.RequestContext;
import edu.stanford.protege.webprotege.dispatch.RequestValidator;
import edu.stanford.protege.webprotege.dispatch.validators.ProjectPermissionValidator;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.project.LoadProjectAction;
import edu.stanford.protege.webprotege.project.LoadProjectResult;
import edu.stanford.protege.webprotege.project.ProjectDetails;
import edu.stanford.protege.webprotege.project.ProjectDetailsManager;
import edu.stanford.protege.webprotege.project.ProjectManager;
import edu.stanford.protege.webprotege.user.UserActivityManager;
import edu.stanford.protege.webprotege.util.MemoryMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

import static edu.stanford.protege.webprotege.access.BuiltInAction.VIEW_PROJECT;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/04/2013
 */
public class LoadProjectActionHandler implements ApplicationActionHandler<LoadProjectAction, LoadProjectResult> {

    private static final Logger logger = LoggerFactory.getLogger(LoadProjectActionHandler.class);

    @Nonnull
    private final ProjectDetailsManager projectDetailsManager;

    @Nonnull
    private final ProjectManager projectManager;

    @Nonnull
    private final AccessManager accessManager;

    @Nonnull
    private final UserActivityManager userActivityManager;

    @Inject
    public LoadProjectActionHandler(@Nonnull ProjectDetailsManager projectDetailsManager,
                                    @Nonnull ProjectManager projectManager,
                                    @Nonnull AccessManager accessManager,
                                    @Nonnull UserActivityManager userActivityManager) {
        this.projectDetailsManager = projectDetailsManager;
        this.accessManager = accessManager;
        this.projectManager = projectManager;
        this.userActivityManager = userActivityManager;
    }

    @Nonnull
    @Override
    public Class<LoadProjectAction> getActionClass() {
        return LoadProjectAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull LoadProjectAction action, @Nonnull RequestContext requestContext) {
        return new ProjectPermissionValidator(accessManager, action.projectId(), requestContext.getUserId(), VIEW_PROJECT
                .getActionId());
    }

    @Nonnull
    @Override
    public LoadProjectResult execute(@Nonnull final LoadProjectAction action, @Nonnull ExecutionContext executionContext) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        logger.info("{} is being loaded due to request by {}",
                    action.projectId(),
                    executionContext.userId());
        projectManager.ensureProjectIsLoaded(action.projectId(), executionContext.userId());
        stopwatch.stop();
        logger.info("{} was loaded in {} ms due to request by {}",
                    action.projectId(),
                    stopwatch.elapsed(TimeUnit.MILLISECONDS),
                    executionContext.userId());
        MemoryMonitor memoryMonitor = new MemoryMonitor(logger);
        memoryMonitor.monitorMemoryUsage();
        memoryMonitor.logMemoryUsage();
        final ProjectId projectId = action.projectId();
        ProjectDetails projectDetails = projectDetailsManager.getProjectDetails(projectId);
        if (!executionContext.userId().isGuest()) {
            userActivityManager.addRecentProject(executionContext.userId(), action.projectId(), System.currentTimeMillis());
        }
        return new LoadProjectResult(action.projectId(),
                                     executionContext.userId(),
                                     projectDetails);
    }
}
