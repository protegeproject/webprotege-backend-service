package edu.stanford.protege.webprotege.dispatch.validators;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.authorization.ActionId;
import edu.stanford.protege.webprotege.authorization.ProjectResource;
import edu.stanford.protege.webprotege.dispatch.RequestValidationResult;
import edu.stanford.protege.webprotege.dispatch.RequestValidator;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Inject;

import static edu.stanford.protege.webprotege.authorization.Subject.forUser;


/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Feb 2017
 */
public class ProjectPermissionValidator implements RequestValidator {

    private final Logger logger = LoggerFactory.getLogger(ProjectPermissionValidator.class);
    private final AccessManager accessManager;

    private final ProjectId projectId;

    private final UserId userId;

    private final ActionId actionId;

    @Inject
    public ProjectPermissionValidator(AccessManager accessManager,
                                      ProjectId projectId,
                                      UserId userId,
                                      ActionId actionId) {
        this.accessManager = accessManager;
        this.projectId = projectId;
        this.userId = userId;
        this.actionId = actionId;
    }

    @Override
    public RequestValidationResult validateAction() {
        if(accessManager.hasPermission(forUser(userId), new ProjectResource(projectId), actionId)) {
            return RequestValidationResult.getValid();
        }
        else {
            return RequestValidationResult.getInvalid("Permission denied for " + actionId.id());
        }
    }
}
