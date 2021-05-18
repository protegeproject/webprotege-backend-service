package edu.stanford.bmir.protege.web.server.permissions;

import edu.stanford.bmir.protege.web.server.dispatch.Action;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.permissions.RebuildPermissionsAction;
import edu.stanford.bmir.protege.web.server.permissions.RebuildPermissionsResult;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
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
