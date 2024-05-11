package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.authorization.ProjectResource;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

import static edu.stanford.protege.webprotege.access.BuiltInRole.*;
import static edu.stanford.protege.webprotege.authorization.Subject.forAnySignedInUser;
import static edu.stanford.protege.webprotege.authorization.Subject.forUser;
import static java.util.Arrays.asList;
import static java.util.Collections.singleton;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-05-07
 */
@Component
public class ProjectPermissionsInitializer {

    private final AccessManager accessManager;

    public ProjectPermissionsInitializer(AccessManager accessManager) {
        this.accessManager = accessManager;
    }

    public CompletableFuture<Void> applyDefaultPermissions(ProjectId projectId, UserId projectOwner) {
        var projectResource = ProjectResource.forProject(projectId);
        // Owner is manager
        return accessManager.setAssignedRolesAsync(forUser(projectOwner),
                                                   projectResource,
                                                   asList(CAN_MANAGE.getRoleId(), PROJECT_DOWNLOADER.getRoleId()))
                            .thenCompose(r -> accessManager.setAssignedRolesAsync(forAnySignedInUser(),
                                                                                  projectResource,
                                                                                  singleton(LAYOUT_EDITOR.getRoleId())))
                            .thenApply(r2 -> null);
    }
}
