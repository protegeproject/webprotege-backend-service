package edu.stanford.protege.webprotege.match;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.match.criteria.CompositeRootCriteria;
import edu.stanford.protege.webprotege.match.criteria.MultiMatchType;
import edu.stanford.protege.webprotege.pagination.Page;
import edu.stanford.protege.webprotege.pagination.PageRequest;
import org.junit.Test;

import java.io.IOException;


import static edu.stanford.protege.webprotege.MockingUtils.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class GetMatchingEntities_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = GetMatchingEntitiesAction.create(mockProjectId(),
                                                      CompositeRootCriteria.get(ImmutableList.of(), MultiMatchType.ALL),
                                                      PageRequest.requestFirstPage());
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = GetMatchingEntitiesResult.create(Page.emptyPage());
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}