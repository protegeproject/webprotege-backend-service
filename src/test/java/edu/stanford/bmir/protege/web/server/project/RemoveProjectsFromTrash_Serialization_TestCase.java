package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.MockingUtils;
import edu.stanford.bmir.protege.web.server.dispatch.Action;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.project.RemoveProjectFromTrashAction;
import edu.stanford.bmir.protege.web.server.project.RemoveProjectFromTrashResult;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
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