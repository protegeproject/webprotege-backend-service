package edu.stanford.protege.webprotege.sharing;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;


import static edu.stanford.protege.webprotege.MockingUtils.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class SetProjectSharingSettings_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = SetProjectSharingSettingsAction.create(new ProjectSharingSettings(
                mockProjectId(),
                Optional.of(SharingPermission.EDIT), ImmutableList.of(
                        new SharingSetting(PersonId.get("User"), SharingPermission.EDIT)
        )
        ));
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = SetProjectSharingSettingsResult.create();
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}