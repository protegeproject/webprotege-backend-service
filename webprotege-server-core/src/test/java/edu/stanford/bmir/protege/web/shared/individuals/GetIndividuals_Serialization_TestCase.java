package edu.stanford.bmir.protege.web.shared.individuals;

import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
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