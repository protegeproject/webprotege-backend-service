package edu.stanford.protege.webprotege.obo;

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
public abstract class SetOboTermDefinition_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = SetOboTermDefinitionAction.create(mockProjectId(),
                                                       mockOWLClass(),
                                                       OBOTermDefinition.empty());
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = SetOboTermDefinitionResult.create();
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}