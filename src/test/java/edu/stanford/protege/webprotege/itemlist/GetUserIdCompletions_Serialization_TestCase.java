package edu.stanford.protege.webprotege.itemlist;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class GetUserIdCompletions_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = GetUserIdCompletionsAction.create("Text");
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = GetUserIdCompletionsResult.create(ImmutableList.of());
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}