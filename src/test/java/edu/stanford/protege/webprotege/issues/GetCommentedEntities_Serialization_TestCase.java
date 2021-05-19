package edu.stanford.protege.webprotege.issues;

import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import edu.stanford.protege.webprotege.pagination.Page;
import edu.stanford.protege.webprotege.pagination.PageRequest;
import edu.stanford.protege.webprotege.project.ProjectId;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-06
 */
public class GetCommentedEntities_Serialization_TestCase {


    @Test
    public void shouldSerializeAction() throws IOException {
        var action = GetCommentedEntitiesAction.create(ProjectId.getNil(),
                                                       "TheUser", Collections.singleton(Status.OPEN),
                                                       SortingKey.SORT_BY_ENTITY,
                                                       PageRequest.requestFirstPage());
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = GetCommentedEntitiesResult.create(ProjectId.getNil(),
                                                       Page.emptyPage());
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}
