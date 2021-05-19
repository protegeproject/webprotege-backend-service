package edu.stanford.protege.webprotege.search;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.lang.LangTagFilter;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import edu.stanford.protege.webprotege.pagination.Page;
import edu.stanford.protege.webprotege.pagination.PageRequest;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;


import static edu.stanford.protege.webprotege.MockingUtils.*;

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