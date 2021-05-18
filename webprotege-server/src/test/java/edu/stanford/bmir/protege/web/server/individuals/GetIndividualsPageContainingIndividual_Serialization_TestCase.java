package edu.stanford.bmir.protege.web.server.individuals;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.dispatch.Action;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.entity.EntityNode;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.server.pagination.Page;
import edu.stanford.bmir.protege.web.server.project.ProjectId;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;


import static edu.stanford.bmir.protege.web.MockingUtils.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class GetIndividualsPageContainingIndividual_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = GetIndividualsPageContainingIndividualAction.create(ProjectId.getNil(),
                                                                         mockOWLNamedIndividual(), Optional.empty(),
                                                                         InstanceRetrievalMode.ALL_INSTANCES);
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = GetIndividualsPageContainingIndividualResult.create(mockOWLNamedIndividual(),
                                                                         Page.emptyPage(), EntityNode.getFromEntityData(mockOWLClassData()),
                                                                         InstanceRetrievalMode.ALL_INSTANCES,
                                                                         ImmutableSet.of());
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}