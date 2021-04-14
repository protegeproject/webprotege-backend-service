package edu.stanford.bmir.protege.web.shared.user;

import edu.stanford.bmir.protege.web.shared.auth.Password;
import edu.stanford.bmir.protege.web.shared.auth.Salt;
import edu.stanford.bmir.protege.web.shared.auth.SaltedPasswordDigest;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-06
 */
public class CreateUserAccount_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = CreateUserAccountAction.create(UserId.getGuest(),
                                                    new EmailAddress("m@m"),
                                                    Password.create("HelloWorld"));
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = CreateUserAccountResult.create();
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}
