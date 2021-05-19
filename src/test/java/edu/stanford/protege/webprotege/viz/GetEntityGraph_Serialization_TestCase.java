package edu.stanford.protege.webprotege.viz;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import edu.stanford.protege.webprotege.project.ProjectId;
import org.junit.Test;

import java.io.IOException;

import static edu.stanford.protege.webprotege.MockingUtils.mockOWLClass;
import static edu.stanford.protege.webprotege.MockingUtils.mockOWLClassData;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class GetEntityGraph_Serialization_TestCase {


    @Test
    public void shouldSerializeAction() throws IOException {
        var action = GetEntityGraphAction.create(ProjectId.getNil(),
                                                 mockOWLClass()); JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = GetEntityGraphResult.get(EntityGraph.create(mockOWLClassData(),
                                                                 ImmutableSet.of(),
                                                                 true),
                                              EntityGraphSettings.get(ImmutableList.of(), 2.5));
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}
