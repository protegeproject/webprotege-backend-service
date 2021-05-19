package edu.stanford.protege.webprotege.dispatch.actions;

import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import edu.stanford.protege.webprotege.ontology.GetRootOntologyIdAction;
import edu.stanford.protege.webprotege.ontology.GetRootOntologyIdResult;
import org.junit.Test;

import java.io.IOException;


import static edu.stanford.protege.webprotege.MockingUtils.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class GetRootOntologyId_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = GetRootOntologyIdAction.create(mockProjectId());
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = GetRootOntologyIdResult.create(mockProjectId(), mockOWLOntologyID());
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}