package edu.stanford.protege.webprotege.dispatch.actions;

import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import edu.stanford.protege.webprotege.ontology.SetOntologyAnnotationsAction;
import edu.stanford.protege.webprotege.ontology.SetOntologyAnnotationsResult;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;


import static edu.stanford.protege.webprotege.MockingUtils.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class SetOntologyAnnotations_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = SetOntologyAnnotationsAction.create(mockProjectId(),
                                                         mockOWLOntologyID(),
                                                         Collections.emptySet(),
                                                         Collections.emptySet());
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = SetOntologyAnnotationsResult.create(Collections.emptySet(),
                                                         mockEventList());
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}