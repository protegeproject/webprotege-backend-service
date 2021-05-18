package edu.stanford.bmir.protege.web.server.search;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.dispatch.Action;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.lang.LangTagFilter;
import edu.stanford.bmir.protege.web.server.search.PerformEntitySearchAction;
import edu.stanford.bmir.protege.web.server.search.PerformEntitySearchResult;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.server.pagination.Page;
import edu.stanford.bmir.protege.web.server.pagination.PageRequest;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;


import static edu.stanford.bmir.protege.web.MockingUtils.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class PerformEntitySearch_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = PerformEntitySearchAction.create(mockProjectId(),
                                                      "Test", Collections.emptySet(),
                                                      LangTagFilter.get(ImmutableSet.of()), ImmutableList.of(),
                                                      PageRequest.requestFirstPage());
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = PerformEntitySearchResult.create("Test", Page.emptyPage());
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}