package edu.stanford.bmir.protege.web.server.renderer;

import edu.stanford.bmir.protege.web.server.dispatch.Action;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.renderer.GetEntityHtmlRenderingAction;
import edu.stanford.bmir.protege.web.server.renderer.GetEntityHtmlRenderingResult;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.server.project.ProjectId;
import org.junit.Test;

import java.io.IOException;

import static edu.stanford.bmir.protege.web.MockingUtils.mockOWLClass;
import static edu.stanford.bmir.protege.web.MockingUtils.mockOWLClassData;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class GetEntityHtmlRendering_Serialization_TestCase {


    @Test
    public void shouldSerializeAction() throws IOException {
        var action = GetEntityHtmlRenderingAction.create(ProjectId.getNil(),
                                                         mockOWLClass()); JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = GetEntityHtmlRenderingResult.create(mockOWLClassData(),
                                                         "TheRendering");
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}
