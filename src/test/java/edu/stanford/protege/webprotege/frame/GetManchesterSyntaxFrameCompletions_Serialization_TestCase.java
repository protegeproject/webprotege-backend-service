package edu.stanford.protege.webprotege.frame;

import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.mansyntax.AutoCompletionResult;
import edu.stanford.protege.webprotege.mansyntax.EditorPosition;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;


import static edu.stanford.protege.webprotege.MockingUtils.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class GetManchesterSyntaxFrameCompletions_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = GetManchesterSyntaxFrameCompletionsAction.create(mockProjectId(),
                                                                      mockOWLClass(),
                                                                      "Blah",
                                                                      new EditorPosition(1, 2),
                                                                      2, Collections.emptySet(),
                                                                      10);
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = GetManchesterSyntaxFrameCompletionsResult.create(new AutoCompletionResult(
                Collections.emptyList(),
                new EditorPosition(1, 2)
        ));
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}