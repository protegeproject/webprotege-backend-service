package edu.stanford.bmir.protege.web.server.issues;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.MockingUtils;
import edu.stanford.bmir.protege.web.server.dispatch.Action;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.server.project.ProjectId;
import org.junit.Test;

import java.io.IOException;

import static com.google.common.collect.ImmutableSet.of;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-06
 */
public class CreateEntityDiscussionThread_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = CreateEntityDiscussionThreadAction.create(ProjectId.getNil(), MockingUtils.mockOWLClass(),
                                                               "The comment");
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = CreateEntityDiscussionThreadResult.create(ImmutableList.of(),
                                                               MockingUtils.mockEventList());
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}
