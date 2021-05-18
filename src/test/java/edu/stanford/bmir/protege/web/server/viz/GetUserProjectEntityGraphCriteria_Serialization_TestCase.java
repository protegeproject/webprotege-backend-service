package edu.stanford.bmir.protege.web.server.viz;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.dispatch.Action;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.viz.EntityGraphSettings;
import edu.stanford.bmir.protege.web.server.viz.GetUserProjectEntityGraphCriteriaAction;
import edu.stanford.bmir.protege.web.server.viz.GetUserProjectEntityGraphCriteriaResult;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;


import static edu.stanford.bmir.protege.web.MockingUtils.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class GetUserProjectEntityGraphCriteria_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = GetUserProjectEntityGraphCriteriaAction.create(mockProjectId());
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = GetUserProjectEntityGraphCriteriaResult.create(mockProjectId(),
                                                                    mockUserId(),
                                                                    EntityGraphSettings.get(ImmutableList.of(), 22));
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}