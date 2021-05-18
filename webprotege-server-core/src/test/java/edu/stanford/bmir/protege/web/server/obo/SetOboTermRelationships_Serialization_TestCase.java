package edu.stanford.bmir.protege.web.server.obo;

import edu.stanford.bmir.protege.web.server.dispatch.Action;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.obo.OBOTermRelationships;
import edu.stanford.bmir.protege.web.server.obo.SetOboTermRelationshipsAction;
import edu.stanford.bmir.protege.web.server.obo.SetOboTermRelationshipsResult;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;


import static edu.stanford.bmir.protege.web.MockingUtils.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class SetOboTermRelationships_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = SetOboTermRelationshipsAction.create(mockProjectId(),
                                                          mockOWLClass(),
                                                          new OBOTermRelationships(Collections.emptySet()
                                                          ));
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = SetOboTermRelationshipsResult.create();
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}