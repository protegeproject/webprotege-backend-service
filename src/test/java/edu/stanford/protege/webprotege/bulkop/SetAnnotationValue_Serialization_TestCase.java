package edu.stanford.protege.webprotege.bulkop;

import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.DataFactory;
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
public class SetAnnotationValue_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = SetAnnotationValueAction.create(mockProjectId(), ImmutableSet.of(),
                                                     mockOWLAnnotationProperty(), DataFactory.getOWLLiteral(33),
                                                     "Test");
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = SetAnnotationValueResult.create(mockEventList());
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}