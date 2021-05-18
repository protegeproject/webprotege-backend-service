package edu.stanford.bmir.protege.web.server.watches;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.dispatch.Action;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.watches.SetEntityWatchesAction;
import edu.stanford.bmir.protege.web.server.watches.SetEntityWatchesResult;
import edu.stanford.bmir.protege.web.server.watches.Watch;
import edu.stanford.bmir.protege.web.server.watches.WatchType;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;


import static edu.stanford.bmir.protege.web.MockingUtils.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class SetEntityWatches_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = SetEntityWatchesAction.create(mockProjectId(),
                                                   mockUserId(),
                                                   mockOWLClass(), ImmutableSet.of(
                        Watch.create(mockUserId(),
                                     mockOWLClass(),
                                     WatchType.ENTITY)
                ));
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = SetEntityWatchesResult.create(mockEventList());
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}