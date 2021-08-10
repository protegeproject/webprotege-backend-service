package edu.stanford.protege.webprotege.app;

import edu.stanford.protege.webprotege.authorization.api.ActionId;
import edu.stanford.protege.webprotege.user.UserDetails;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

import static edu.stanford.protege.webprotege.app.UserInSessionEncoding.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28/12/14
 */
public class UserInSessionEncoder implements ClientObjectEncoder<UserInSession> {

    @Override
    public JsonObject encode(UserInSession object) {
        UserDetails userDetails = object.getUserDetails();
        JsonArrayBuilder actionArray = Json.createArrayBuilder();
        object.getAllowedApplicationActions().stream().map(ActionId::id).forEach(actionArray::add);
        return Json.createObjectBuilder()
                   .add(USER_NAME, userDetails.getUserId().id())
                   .add(DISPLAY_NAME, userDetails.getDisplayName())
                   .add(USER_EMAIL, userDetails.getEmailAddress().orElse(""))
                   .add(APPLICATION_ACTIONS, actionArray)
                .build();
    }
}
