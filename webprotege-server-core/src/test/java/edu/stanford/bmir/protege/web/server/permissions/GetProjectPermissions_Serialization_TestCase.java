package edu.stanford.bmir.protege.web.server.permissions;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.dispatch.Action;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.permissions.GetProjectPermissionsAction;
import edu.stanford.bmir.protege.web.server.permissions.GetProjectPermissionsResult;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;


import static edu.stanford.bmir.protege.web.MockingUtils.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class GetProjectPermissions_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = GetProjectPermissionsAction.create(mockProjectId(),
                                                        mockUserId());
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = GetProjectPermissionsResult.create(ImmutableSet.of());
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}