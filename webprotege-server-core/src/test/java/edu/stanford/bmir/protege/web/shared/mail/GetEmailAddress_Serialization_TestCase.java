package edu.stanford.bmir.protege.web.shared.mail;

import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.bmir.protege.web.MockingUtils;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public abstract class GetEmailAddress_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = GetEmailAddressAction.create(MockingUtils.mockUserId());
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = GetEmailAddressResult.create(MockingUtils.mockUserId(), null);
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}
