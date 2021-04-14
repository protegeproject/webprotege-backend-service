package edu.stanford.bmir.protege.web.shared.auth;

import edu.stanford.bmir.protege.web.shared.access.ActionId;
import edu.stanford.bmir.protege.web.shared.app.UserInSession;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;


import static edu.stanford.bmir.protege.web.MockingUtils.*;

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