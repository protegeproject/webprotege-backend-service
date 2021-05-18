package edu.stanford.bmir.protege.web.server.merge_add;

import edu.stanford.bmir.protege.web.server.csv.DocumentId;
import edu.stanford.bmir.protege.web.server.dispatch.Action;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.merge_add.GetAllOntologiesAction;
import edu.stanford.bmir.protege.web.server.merge_add.GetAllOntologiesResult;
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
