package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.server.dispatch.Action;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.project.MoveProjectsToTrashAction;
import edu.stanford.bmir.protege.web.server.project.MoveProjectsToTrashResult;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;


import static edu.stanford.bmir.protege.web.MockingUtils.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class MoveProjectsToTrash_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = MoveProjectsToTrashAction.create(mockProjectId());
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = MoveProjectsToTrashResult.create(mockEventList());
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}