package edu.stanford.bmir.protege.web.server.merge;

import edu.stanford.bmir.protege.web.server.csv.DocumentId;
import edu.stanford.bmir.protege.web.server.dispatch.Action;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.merge.ComputeProjectMergeAction;
import edu.stanford.bmir.protege.web.server.merge.ComputeProjectMergeResult;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.server.project.ProjectId;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-06
 */
public class ComputeProjectMerge_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = ComputeProjectMergeAction.create(ProjectId.getNil(),
                                                      new DocumentId(UUID.randomUUID().toString()));
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = ComputeProjectMergeResult.create(Collections.emptyList());
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}
