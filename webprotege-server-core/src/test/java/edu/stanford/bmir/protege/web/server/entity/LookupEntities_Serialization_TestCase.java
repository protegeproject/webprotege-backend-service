package edu.stanford.bmir.protege.web.server.entity;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.dispatch.Action;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.server.search.SearchResultMatch;
import edu.stanford.bmir.protege.web.server.search.SearchResultMatchPosition;
import edu.stanford.bmir.protege.web.server.shortform.DictionaryLanguage;
import org.junit.Test;

import java.io.IOException;


import static edu.stanford.bmir.protege.web.MockingUtils.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class LookupEntities_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = LookupEntitiesAction.create(mockProjectId(),
                                                 new EntityLookupRequest("Search"));
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = LookupEntitiesResult.create(ImmutableList.of(
                EntityLookupResult.get(SearchResultMatch.get(mockOWLClassNode(),
                                                             DictionaryLanguage.localName(),
                                                             "Search",
                                                             ImmutableList.of(SearchResultMatchPosition.get(2, 4))),
                                       "link")
        ));
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}