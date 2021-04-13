package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.gwtcodemirror.client.AutoCompletionResult;
import edu.stanford.bmir.gwtcodemirror.client.EditorPosition;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;


import static edu.stanford.bmir.protege.web.MockingUtils.*;

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