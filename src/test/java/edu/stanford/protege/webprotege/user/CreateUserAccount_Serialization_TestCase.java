package edu.stanford.protege.webprotege.user;

import edu.stanford.protege.webprotege.auth.Password;
import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
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
