package edu.stanford.bmir.protege.web.server.obo;

import edu.stanford.bmir.protege.web.server.dispatch.Action;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.obo.GetOboNamespacesAction;
import edu.stanford.bmir.protege.web.server.obo.GetOboNamespacesResult;
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
public class GetOboNamespaces_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = GetOboNamespacesAction.create(mockProjectId());
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = GetOboNamespacesResult.create(Collections.emptySet());
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}