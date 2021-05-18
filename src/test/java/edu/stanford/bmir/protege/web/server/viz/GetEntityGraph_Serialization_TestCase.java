package edu.stanford.bmir.protege.web.server.viz;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.dispatch.Action;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.viz.EntityGraph;
import edu.stanford.bmir.protege.web.server.viz.EntityGraphSettings;
import edu.stanford.bmir.protege.web.server.viz.GetEntityGraphAction;
import edu.stanford.bmir.protege.web.server.viz.GetEntityGraphResult;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.server.project.ProjectId;
import org.junit.Test;

import java.io.IOException;

import static edu.stanford.bmir.protege.web.MockingUtils.mockOWLClass;
import static edu.stanford.bmir.protege.web.MockingUtils.mockOWLClassData;

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
