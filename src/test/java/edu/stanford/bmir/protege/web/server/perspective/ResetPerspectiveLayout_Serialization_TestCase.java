package edu.stanford.bmir.protege.web.server.perspective;

import edu.stanford.bmir.protege.web.server.dispatch.Action;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.server.perspective.PerspectiveLayout;
import edu.stanford.bmir.protege.web.server.perspective.ResetPerspectiveLayoutAction;
import edu.stanford.bmir.protege.web.server.perspective.ResetPerspectiveLayoutResult;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;


import static edu.stanford.bmir.protege.web.MockingUtils.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class ResetPerspectiveLayout_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = ResetPerspectiveLayoutAction.create(mockProjectId(),
                                                         PerspectiveId.generate());
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = ResetPerspectiveLayoutResult.create(PerspectiveLayout.get(PerspectiveId.generate()));
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}