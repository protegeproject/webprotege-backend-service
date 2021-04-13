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
public class ChangePassword_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = new ChangePasswordAction(
                UserId.getGuest(),
                new ChapSessionId("id"),
                new ChapResponse(new byte[]{1, 2, 3, 4}),
                new SaltedPasswordDigest(new byte []{1, 2, 3, 4}),
                new Salt(new byte []{1, 2, 3, 4})
        );
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = new ChangePasswordResult(AuthenticationResponse.SUCCESS);
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}
