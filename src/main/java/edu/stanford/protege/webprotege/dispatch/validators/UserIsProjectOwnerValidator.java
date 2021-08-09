package edu.stanford.protege.webprotege.dispatch.validators;


import edu.stanford.protege.webprotege.dispatch.RequestValidationResult;
import edu.stanford.protege.webprotege.dispatch.RequestValidator;
import edu.stanford.protege.webprotege.project.ProjectDetailsManager;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 07/02/15
 */
public class UserIsProjectOwnerValidator implements RequestValidator {

    private ProjectDetailsManager projectDetailsManager;

    private UserId userId;

    private ProjectId projectId;

    @Inject
    public UserIsProjectOwnerValidator(ProjectId projectId,
                                       UserId userId,
                                       ProjectDetailsManager projectDetailsManager) {
        this.projectDetailsManager = projectDetailsManager;
        this.userId = userId;
        this.projectId = projectId;
    }

    @Override
    public RequestValidationResult validateAction() {
        boolean projectOwner = projectDetailsManager.isProjectOwner(userId, projectId);
        if(projectOwner) {
            return RequestValidationResult.getValid();
        }
        else {
            return RequestValidationResult.getInvalid("Only project owners can perform the requested operation.");
        }
    }
}
