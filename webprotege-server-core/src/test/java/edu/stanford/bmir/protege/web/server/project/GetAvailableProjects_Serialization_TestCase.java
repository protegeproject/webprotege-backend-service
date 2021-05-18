package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.server.dispatch.Action;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.project.GetAvailableProjectsAction;
import edu.stanford.bmir.protege.web.server.project.GetAvailableProjectsResult;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-06
 */
public class GetAvailableProjects_Serialization_TestCase {


    @Test
    public void shouldSerializeAction() throws IOException {
        var action = GetAvailableProjectsAction.create();
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = GetAvailableProjectsResult.create(Collections.emptyList());
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }

}
