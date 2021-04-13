package edu.stanford.bmir.protege.web.shared.permissions;

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
public class RebuildPermissions_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        JsonSerializationTestUtil.testSerialization(RebuildPermissionsAction.get(),
                                                    Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        JsonSerializationTestUtil.testSerialization(RebuildPermissionsResult.get(),
                                                    Result.class);
    }
}
