package edu.stanford.protege.webprotege.frame;

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
public class SetManchesterSyntaxFrame_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = SetManchesterSyntaxFrameAction.create(mockProjectId(),
                                                           mockOWLClass(),
                                                           "From",
                                                           "To", Collections.emptySet(),
                                                           Optional.empty());
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = SetManchesterSyntaxFrameResult.create(mockEventList(), "Frame");
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}