package edu.stanford.protege.webprotege.issues;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import edu.stanford.protege.webprotege.MockingUtils;
import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.entity.OWLClassData;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import edu.stanford.protege.webprotege.project.ProjectId;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class GetEntityDiscussionThreads_Serialization_TestCase {


    @Test
    public void shouldSerializeAction() throws IOException {
        var action = GetEntityDiscussionThreadsAction.create(ProjectId.getNil(), MockingUtils.mockOWLClass());
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = GetEntityDiscussionThreadsResult.create(OWLClassData.get(
                MockingUtils.mockOWLClass(),
                ImmutableMap.of(),
                false
        ), ImmutableList.of());
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}
