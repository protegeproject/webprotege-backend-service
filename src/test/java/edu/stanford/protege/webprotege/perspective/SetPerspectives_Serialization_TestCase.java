package edu.stanford.protege.webprotege.perspective;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.lang.LanguageMap;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;


import static edu.stanford.protege.webprotege.MockingUtils.*;

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