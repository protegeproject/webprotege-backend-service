package edu.stanford.bmir.protege.web.server.obo;

import edu.stanford.bmir.protege.web.server.dispatch.Action;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.obo.GetOboTermSynonymsAction;
import edu.stanford.bmir.protege.web.server.obo.GetOboTermSynonymsResult;
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
public class GetOboTermSynonyms_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = GetOboTermSynonymsAction.create(mockProjectId(),
                                                     mockOWLClass());
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = GetOboTermSynonymsResult.create(Collections.emptyList());
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}