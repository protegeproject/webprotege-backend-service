package edu.stanford.protege.webprotege.mail;

import edu.stanford.protege.webprotege.dispatch.RequestValidationResult;
import edu.stanford.protege.webprotege.dispatch.RequestValidator;
import edu.stanford.protege.webprotege.common.UserId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/11/2013
 */
public class SetEmailAddressRequestValidator implements RequestValidator {

    private final UserId userId;

    private final UserId loggedInUserId;

    public SetEmailAddressRequestValidator(UserId userId, UserId loggedInUserId) {
        this.userId = userId;
        this.loggedInUserId = loggedInUserId;
    }


    @Override
    public RequestValidationResult validateAction() {
        if(userId.equals(loggedInUserId)) {
            return RequestValidationResult.getValid();
        }
        else {
            return RequestValidationResult.getInvalid("Users can only change their own email addresses");
        }
    }
}
