package edu.stanford.bmir.protege.web.server.chgpwd;

import edu.stanford.bmir.protege.web.server.dispatch.Action;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class ResetPassword_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = ResetPasswordAction.create(new ResetPasswordData("test"));
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = ResetPasswordResult.create(ResetPasswordResultCode.SUCCESS);
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}