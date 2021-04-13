package edu.stanford.bmir.protege.web.shared.project;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.access.ActionId;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import static edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil.testSerialization;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
@RunWith(MockitoJUnitRunner.class)
public class GetAvailableProjectsWithPermission_Serialization_TestCase {


    @Test
    public void shouldSerializeAction() throws IOException {
        var action = GetAvailableProjectsWithPermissionAction.create(new ActionId("Something"));
        testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = GetAvailableProjectsWithPermissionResult.create(ImmutableList.of());
        testSerialization(result, Result.class);
    }
}
