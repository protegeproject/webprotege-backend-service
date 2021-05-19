package edu.stanford.protege.webprotege.individuals;

import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.entity.EntityNode;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import edu.stanford.protege.webprotege.pagination.Page;
import edu.stanford.protege.webprotege.project.ProjectId;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;


import static edu.stanford.protege.webprotege.MockingUtils.*;

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