package edu.stanford.protege.webprotege.change;

import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import edu.stanford.protege.webprotege.revision.RevisionNumber;
import org.junit.Test;

import java.io.IOException;


import static edu.stanford.protege.webprotege.MockingUtils.*;

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