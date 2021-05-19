package edu.stanford.protege.webprotege.watches;

import com.google.common.collect.ImmutableSet;
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