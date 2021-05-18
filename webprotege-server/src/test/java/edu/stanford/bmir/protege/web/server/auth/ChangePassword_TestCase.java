package edu.stanford.bmir.protege.web.server.auth;

import edu.stanford.bmir.protege.web.MockingUtils;
import edu.stanford.bmir.protege.web.server.dispatch.Action;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
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
        var action = ChangePasswordAction.create(
                MockingUtils.mockUserId(),
                Password.create("Current"),
                Password.create("Next")
        );
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = ChangePasswordResult.create(AuthenticationResponse.SUCCESS);
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}
