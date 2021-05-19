package edu.stanford.protege.webprotege.dispatch.actions;

import edu.stanford.protege.webprotege.app.UserInSession;
import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import edu.stanford.protege.webprotege.user.GetCurrentUserInSessionAction;
import edu.stanford.protege.webprotege.user.GetCurrentUserInSessionResult;
import edu.stanford.protege.webprotege.user.UserDetails;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class GetCurrentUserInSession_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = GetCurrentUserInSessionAction.create();
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = GetCurrentUserInSessionResult.create(new UserInSession(UserDetails.getGuestUserDetails(),
                                                                            Collections.emptySet()));
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}
