package edu.stanford.bmir.protege.web.server.obo;

import edu.stanford.bmir.protege.web.server.dispatch.Action;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.obo.OBOTermCrossProduct;
import edu.stanford.bmir.protege.web.server.obo.SetOboTermCrossProductAction;
import edu.stanford.bmir.protege.web.server.obo.SetOboTermCrossProductResult;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;


import static edu.stanford.bmir.protege.web.MockingUtils.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class SetOboTermCrossProduct_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = SetOboTermCrossProductAction.create(mockProjectId(),
                                                         mockOWLClass(),
                                                         OBOTermCrossProduct.emptyOBOTermCrossProduct());
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = SetOboTermCrossProductResult.create();
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}