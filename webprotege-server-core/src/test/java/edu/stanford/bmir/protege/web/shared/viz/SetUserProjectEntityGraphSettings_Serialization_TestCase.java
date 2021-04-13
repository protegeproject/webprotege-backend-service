package edu.stanford.bmir.protege.web.shared.viz;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;


import static edu.stanford.bmir.protege.web.MockingUtils.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public abstract class SetUserProjectEntityGraphSettings_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = SetUserProjectEntityGraphSettingsAction.create(mockProjectId(),
                                                                    mockUserId(),
                                                                    EntityGraphSettings.get(
                                                                            ImmutableList.of(),
                                                                            2
                                                                    ));
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = SetUserProjectEntityGraphSettingsResult.create();
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}