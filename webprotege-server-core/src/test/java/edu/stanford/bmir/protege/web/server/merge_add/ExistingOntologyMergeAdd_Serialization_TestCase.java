package edu.stanford.bmir.protege.web.server.merge_add;

import edu.stanford.bmir.protege.web.MockingUtils;
import edu.stanford.bmir.protege.web.server.csv.DocumentId;
import edu.stanford.bmir.protege.web.server.dispatch.Action;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.merge_add.ExistingOntologyMergeAddAction;
import edu.stanford.bmir.protege.web.server.merge_add.ExistingOntologyMergeAddResult;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.server.project.ProjectId;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-06
 */
public class ExistingOntologyMergeAdd_Serialization_TestCase {


    @Test
    public void shouldSerializeAction() throws IOException {
        var action = ExistingOntologyMergeAddAction.create(ProjectId.getNil(),
                                                           new DocumentId("abc"),
                                                           Collections.emptyList(),
                                                           MockingUtils.mockOWLOntologyID());
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = ExistingOntologyMergeAddResult.create();
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}
