package edu.stanford.protege.webprotege.merge;

import edu.stanford.protege.webprotege.csv.DocumentId;
import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import edu.stanford.protege.webprotege.project.ProjectId;
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
