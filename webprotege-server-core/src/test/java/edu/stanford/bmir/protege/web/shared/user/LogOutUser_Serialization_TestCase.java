package edu.stanford.bmir.protege.web.shared.user;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.app.UserInSession;
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
public class LogOutUser_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = new LogOutUserAction();
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = new LogOutUserResult(new UserInSession(
                UserDetails.getGuestUserDetails(), ImmutableSet.of()
        ));
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}
