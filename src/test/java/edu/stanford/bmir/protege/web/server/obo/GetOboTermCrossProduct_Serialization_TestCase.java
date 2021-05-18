package edu.stanford.bmir.protege.web.server.obo;

import edu.stanford.bmir.protege.web.server.dispatch.Action;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.obo.GetOboTermCrossProductAction;
import edu.stanford.bmir.protege.web.server.obo.GetOboTermCrossProductResult;
import edu.stanford.bmir.protege.web.server.obo.OBOTermCrossProduct;
import edu.stanford.bmir.protege.web.server.obo.OBOTermRelationships;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;


import static edu.stanford.bmir.protege.web.MockingUtils.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class GetOboTermCrossProduct_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = GetOboTermCrossProductAction.create(mockProjectId(),
                                                         mockOWLClass());
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = GetOboTermCrossProductResult.create(new OBOTermCrossProduct(Optional.empty(),
                                                                                 new OBOTermRelationships(Collections.emptySet())
        ));
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}