package edu.stanford.bmir.protege.web.shared.search;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRootCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.MultiMatchType;
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