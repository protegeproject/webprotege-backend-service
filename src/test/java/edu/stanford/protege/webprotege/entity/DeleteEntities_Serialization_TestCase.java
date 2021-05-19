package edu.stanford.protege.webprotege.entity;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.MockingUtils;
import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import edu.stanford.protege.webprotege.project.ProjectId;
import org.junit.Test;

import java.io.IOException;


/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-06
 */
public class DeleteEntities_Serialization_TestCase {


    @Test
    public void shouldSerializeAction() throws IOException {
        var action = new DeleteEntitiesAction(ProjectId.getNil(),
                                              ImmutableSet.of());
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = new DeleteEntitiesResult(MockingUtils.mockEventList(),
                                              ImmutableSet.of());
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}
