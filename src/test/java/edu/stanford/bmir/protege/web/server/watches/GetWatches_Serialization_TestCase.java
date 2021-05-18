package edu.stanford.bmir.protege.web.server.watches;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.dispatch.Action;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.watches.GetWatchesAction;
import edu.stanford.bmir.protege.web.server.watches.GetWatchesResult;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;


import static edu.stanford.bmir.protege.web.MockingUtils.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class GetWatches_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = GetWatchesAction.create(mockProjectId(),
                                             mockUserId(),
                                             mockOWLClass());
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = GetWatchesResult.create(ImmutableSet.of());
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}