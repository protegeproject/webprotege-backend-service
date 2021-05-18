package edu.stanford.bmir.protege.web.server.tag;

import edu.stanford.bmir.protege.web.server.dispatch.Action;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.tag.GetEntityTagsAction;
import edu.stanford.bmir.protege.web.server.tag.GetEntityTagsResult;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.server.project.ProjectId;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;

import static edu.stanford.bmir.protege.web.MockingUtils.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class GetEntityTags_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = GetEntityTagsAction.create(ProjectId.getNil(),
                                                mockOWLClass());
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = GetEntityTagsResult.create(Collections.emptySet(),
                                                Collections.emptySet());
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}