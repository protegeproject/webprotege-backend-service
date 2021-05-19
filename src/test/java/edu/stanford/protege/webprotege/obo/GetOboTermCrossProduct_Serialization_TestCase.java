package edu.stanford.protege.webprotege.obo;

import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;


import static edu.stanford.protege.webprotege.MockingUtils.*;

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