package edu.stanford.protege.webprotege.dispatch;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-15
 */
public class Batch_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = BatchAction.create(ImmutableList.of());
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = BatchResult.get(ImmutableList.of());
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}