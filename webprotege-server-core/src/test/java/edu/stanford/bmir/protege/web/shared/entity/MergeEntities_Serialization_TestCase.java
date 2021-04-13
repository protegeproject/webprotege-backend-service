package edu.stanford.bmir.protege.web.shared.entity;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;


import static edu.stanford.bmir.protege.web.MockingUtils.*;

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