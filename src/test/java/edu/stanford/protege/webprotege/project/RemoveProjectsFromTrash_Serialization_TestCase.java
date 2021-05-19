package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.MockingUtils;
import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class RemoveProjectsFromTrash_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = RemoveProjectFromTrashAction.create(MockingUtils.mockProjectId());
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = RemoveProjectFromTrashResult.create(MockingUtils.mockEventList());
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}