package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.authorization.ApplicationResource;
import edu.stanford.protege.webprotege.authorization.ProjectResource;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.dispatch.ApplicationActionHandler;
import edu.stanford.protege.webprotege.dispatch.RequestContext;
import edu.stanford.protege.webprotege.dispatch.RequestValidator;
import edu.stanford.protege.webprotege.dispatch.validators.ApplicationPermissionValidator;
import edu.stanford.protege.webprotege.dispatch.validators.CompositeRequestValidator;
import edu.stanford.protege.webprotege.dispatch.validators.UserIsSignedInValidator;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.permissions.PermissionDeniedException;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.protege.webprotege.access.BuiltInAction.CREATE_EMPTY_PROJECT;
import static edu.stanford.protege.webprotege.access.BuiltInAction.UPLOAD_PROJECT;
import static edu.stanford.protege.webprotege.access.BuiltInRole.CAN_MANAGE;
import static edu.stanford.protege.webprotege.access.BuiltInRole.LAYOUT_EDITOR;
import static edu.stanford.protege.webprotege.access.BuiltInRole.PROJECT_DOWNLOADER;
import static edu.stanford.protege.webprotege.authorization.Subject.forAnySignedInUser;
import static edu.stanford.protege.webprotege.authorization.Subject.forUser;
import static java.util.Arrays.asList;
import static java.util.Collections.singleton;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
public class CreateNewProjectActionHandler implements ApplicationActionHandler<CreateNewProjectAction, CreateNewProjectResult> {

    private static Logger logger = LoggerFactory.getLogger(CreateNewProjectActionHandler.class);

    private final ProjectManager pm;

    private final ProjectDetailsManager projectDetailsManager;

    private final AccessManager accessManager;

    @Inject
    public CreateNewProjectActionHandler(@Nonnull ProjectManager pm,
                                         @Nonnull ProjectDetailsManager projectDetailsManager,
                                         @Nonnull AccessManager accessManager) {
        this.pm = checkNotNull(pm);
        this.projectDetailsManager = checkNotNull(projectDetailsManager);
        this.accessManager = checkNotNull(accessManager);
    }

    @Nonnull
    @Override
    public Class<CreateNewProjectAction> getActionClass() {
        return CreateNewProjectAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull CreateNewProjectAction action, @Nonnull RequestContext requestContext) {
        return new CompositeRequestValidator(
                new UserIsSignedInValidator(requestContext.getUserId()),
                new ApplicationPermissionValidator(
                        accessManager,
                        requestContext.getUserId(),
                        CREATE_EMPTY_PROJECT,
                        requestContext.getExecutionContext())
        );
    }

    @Nonnull
    @Override
    public CreateNewProjectResult execute(@Nonnull CreateNewProjectAction action, @Nonnull ExecutionContext executionContext) {
        var projectId = action.newProjectId();
        try {

            var userId = executionContext.userId();
            if (!accessManager.hasPermission(forUser(userId), ApplicationResource.get(), CREATE_EMPTY_PROJECT.getActionId(), executionContext)) {
                throw new PermissionDeniedException("You do not have permission to create new projects");
            }
            var newProjectSettings = action.newProjectSettings();
            if (newProjectSettings.hasSourceDocument()) {
                if (!accessManager.hasPermission(forUser(userId), ApplicationResource.get(), UPLOAD_PROJECT.getActionId(), executionContext)) {
                    throw new PermissionDeniedException("You do not have permission to upload projects");
                }
            }
            if (!projectDetailsManager.isExistingProject(projectId)) {
                pm.createNewProject(projectId, newProjectSettings, executionContext);
                projectDetailsManager.registerProject(projectId, newProjectSettings);
                applyDefaultPermissions(projectId, userId);
            }
            return new CreateNewProjectResult(projectDetailsManager.getProjectDetails(projectId));
        } catch (OWLOntologyCreationException | OWLOntologyStorageException | IOException e) {
            logger.warn("Error when creating project: {}", e.getMessage(), e);
            throw new ProjectCreationException(projectId, "An error occurred when creating the project: " + e.getMessage());
        }
    }

    private void applyDefaultPermissions(ProjectId projectId, UserId userId) {
        var projectResource = new ProjectResource(projectId);
        // Owner is manager
        accessManager.setAssignedRoles(forUser(userId),
                                       projectResource,
                                       asList(CAN_MANAGE.getRoleId(), PROJECT_DOWNLOADER.getRoleId()));
        // Any signed in user can edit the layout
        accessManager.setAssignedRoles(forAnySignedInUser(),
                                       projectResource,
                                       singleton(LAYOUT_EDITOR.getRoleId()));
    }


}
