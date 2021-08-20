package edu.stanford.protege.webprotege.merge_add;

import edu.stanford.protege.webprotege.csv.DocumentId;
import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-06
 */
public class GetUploadedAndProjectOntologyIds_Action_Serialization_TestCase {


    @Test
    public void shouldSerializeAction() throws IOException {
        var action = GetUploadedAndProjectOntologyIdsAction.create(ProjectId.getNil(),
                                                                   new DocumentId("abc"));
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = GetUploadedAndProjectOntologyIdsResult.create(Collections.emptyList());
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}
