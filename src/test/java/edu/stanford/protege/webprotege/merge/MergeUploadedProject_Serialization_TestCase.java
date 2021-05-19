package edu.stanford.protege.webprotege.merge;

import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;


import static edu.stanford.protege.webprotege.MockingUtils.*;

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