package edu.stanford.bmir.protege.web.server.search;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.dispatch.Action;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.lang.LanguageMap;
import edu.stanford.bmir.protege.web.server.search.EntitySearchFilter;
import edu.stanford.bmir.protege.web.server.search.EntitySearchFilterId;
import edu.stanford.bmir.protege.web.server.search.SetSearchSettingsAction;
import edu.stanford.bmir.protege.web.server.search.SetSearchSettingsResult;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.server.match.criteria.CompositeRootCriteria;
import edu.stanford.bmir.protege.web.server.match.criteria.MultiMatchType;
import org.junit.Test;

import java.io.IOException;


import static edu.stanford.bmir.protege.web.MockingUtils.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class SetSearchSettings_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = SetSearchSettingsAction.create(mockProjectId(), ImmutableList.of(
                EntitySearchFilter.get(EntitySearchFilterId.createFilterId(),
                                       mockProjectId(),
                                       LanguageMap.of("en", "Test"), CompositeRootCriteria.get(ImmutableList.of(),
                                                                                               MultiMatchType.ALL))
        ), ImmutableList.of());
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = SetSearchSettingsResult.create();
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}