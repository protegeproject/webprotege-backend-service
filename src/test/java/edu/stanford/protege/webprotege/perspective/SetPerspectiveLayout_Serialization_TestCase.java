package edu.stanford.protege.webprotege.perspective;

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
public class SetPerspectiveLayout_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = SetPerspectiveLayoutAction.create(mockProjectId(),
                                                       mockUserId(),
                                                       PerspectiveLayout.get(PerspectiveId.generate()));
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = SetPerspectiveLayoutResult.create();
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}