package edu.stanford.protege.webprotege.entity;

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
public class MergeEntities_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = MergeEntitiesAction.create(mockProjectId(), ImmutableSet.of(),
                                                mockOWLClass(),
                                                MergedEntityTreatment.DELETE_MERGED_ENTITY,
                                                "Test");
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = MergeEntitiesResult.create(mockEventList());
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}