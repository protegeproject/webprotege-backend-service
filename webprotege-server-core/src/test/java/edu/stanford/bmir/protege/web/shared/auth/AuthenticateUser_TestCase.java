package edu.stanford.bmir.protege.web.shared.auth;

import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-05
 */
public class AuthenticateUser_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = new AuthenticateUserAction(UserId.getGuest(),
                                                new ChapSessionId("TheId"),
                                                new ChapResponse(new byte[] {1, 2, 3, 4, 5, 6, 7, 8}));
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = new AuthenticateUserResult(AuthenticationResponse.FAIL);
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}
