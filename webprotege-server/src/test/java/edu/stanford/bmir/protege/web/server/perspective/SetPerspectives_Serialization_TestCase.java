package edu.stanford.bmir.protege.web.server.perspective;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.dispatch.Action;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.lang.LanguageMap;
import edu.stanford.bmir.protege.web.server.perspective.PerspectiveDescriptor;
import edu.stanford.bmir.protege.web.server.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.server.perspective.SetPerspectivesAction;
import edu.stanford.bmir.protege.web.server.perspective.SetPerspectivesResult;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;


import static edu.stanford.bmir.protege.web.MockingUtils.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class SetPerspectives_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = SetPerspectivesAction.create(mockProjectId(),
                                                  mockUserId(), ImmutableList.of(
                        PerspectiveDescriptor.get(PerspectiveId.generate(),
                                                  LanguageMap.of("en", "Hello"),
                                                  true)
                ));
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = SetPerspectivesResult.create(ImmutableList.of(), ImmutableSet.of());
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}