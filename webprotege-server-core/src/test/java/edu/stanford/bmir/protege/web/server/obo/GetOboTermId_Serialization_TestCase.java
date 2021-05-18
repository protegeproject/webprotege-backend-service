package edu.stanford.bmir.protege.web.server.obo;

import edu.stanford.bmir.protege.web.server.dispatch.Action;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.obo.GetOboTermIdAction;
import edu.stanford.bmir.protege.web.server.obo.GetOboTermIdResult;
import edu.stanford.bmir.protege.web.server.obo.OBOTermId;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;


import static edu.stanford.bmir.protege.web.MockingUtils.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class GetOboTermId_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = GetOboTermIdAction.create(mockProjectId(), mockOWLClass());
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = GetOboTermIdResult.create(mockOWLClass(), new OBOTermId("id", "name", "ns"));
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}