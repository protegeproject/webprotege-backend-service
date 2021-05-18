package edu.stanford.bmir.protege.web.server.change;

import edu.stanford.bmir.protege.web.server.dispatch.Action;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.server.revision.RevisionNumber;
import org.junit.Test;

import java.io.IOException;


import static edu.stanford.bmir.protege.web.MockingUtils.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class RevertRevision_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = RevertRevisionAction.create(mockProjectId(),
                                                 RevisionNumber.getHeadRevisionNumber());
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = RevertRevisionResult.create(mockProjectId(),
                                                 RevisionNumber.getHeadRevisionNumber(),
                                                 mockEventList());
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}