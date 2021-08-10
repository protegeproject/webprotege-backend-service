package edu.stanford.protege.webprotege.auth;

import edu.stanford.protege.webprotege.authorization.api.ActionId;
import edu.stanford.protege.webprotege.app.UserInSession;
import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import edu.stanford.protege.webprotege.user.UserDetails;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;


import static edu.stanford.protege.webprotege.MockingUtils.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class PerformLogin_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = PerformLoginAction.create(mockUserId(),
                                               Password.create("Hello"));
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = PerformLoginResult.create(AuthenticationResponse.SUCCESS,
                                               new UserInSession(
                                                       new UserDetails(
                                                               mockUserId(),
                                                               "DisplayName", Optional.empty()
                                                       ),
                                                       Collections.singleton(new ActionId("Something"))
                                               ));
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}