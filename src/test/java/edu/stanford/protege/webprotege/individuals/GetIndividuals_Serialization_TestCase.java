package edu.stanford.protege.webprotege.individuals;

import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import edu.stanford.protege.webprotege.pagination.Page;
import edu.stanford.protege.webprotege.project.ProjectId;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class GetIndividuals_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = GetIndividualsAction.create(ProjectId.getNil(), Optional.empty(),
                                                 "Hello",
                                                 InstanceRetrievalMode.ALL_INSTANCES,
                                                 Optional.empty());
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = GetIndividualsResult.create(Optional.empty(),
                                                 Page.emptyPage());
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}