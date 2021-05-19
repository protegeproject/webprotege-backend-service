package edu.stanford.protege.webprotege.merge_add;

import edu.stanford.protege.webprotege.csv.DocumentId;
import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import edu.stanford.protege.webprotege.project.ProjectId;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-06
 */
public class GetAllOntologies_Serialization_TestCase {


    @Test
    public void shouldSerializeAction() throws IOException {
        var action = GetAllOntologiesAction.create(ProjectId.getNil(),
                                                   new DocumentId("abc"));
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = GetAllOntologiesResult.create(Collections.emptyList());
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}
