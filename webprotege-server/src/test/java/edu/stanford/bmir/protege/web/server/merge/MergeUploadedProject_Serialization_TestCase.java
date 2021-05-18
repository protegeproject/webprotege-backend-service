package edu.stanford.bmir.protege.web.server.merge;

import edu.stanford.bmir.protege.web.server.dispatch.Action;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.merge.MergeUploadedProjectAction;
import edu.stanford.bmir.protege.web.server.merge.MergeUploadedProjectResult;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;


import static edu.stanford.bmir.protege.web.MockingUtils.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class MergeUploadedProject_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = MergeUploadedProjectAction.create(mockProjectId(),
                                                       mockDocumentId(),
                                                       "Test");
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = MergeUploadedProjectResult.create();
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}