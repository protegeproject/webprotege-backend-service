package edu.stanford.bmir.protege.web.shared.dispatch;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;


import static edu.stanford.bmir.protege.web.MockingUtils.*;

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