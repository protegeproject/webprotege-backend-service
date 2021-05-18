package edu.stanford.bmir.protege.web.server.obo;

import edu.stanford.bmir.protege.web.server.dispatch.Action;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.obo.OBOTermId;
import edu.stanford.bmir.protege.web.server.obo.SetOboTermIdAction;
import edu.stanford.bmir.protege.web.server.obo.SetOboTermIdResult;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;


import static edu.stanford.bmir.protege.web.MockingUtils.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public abstract class SetOboTermId_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = SetOboTermIdAction.create(mockProjectId(),
                                               mockOWLClass(),
                                               new OBOTermId("123", "Hello", "Blah"));
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = SetOboTermIdResult.create();
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}