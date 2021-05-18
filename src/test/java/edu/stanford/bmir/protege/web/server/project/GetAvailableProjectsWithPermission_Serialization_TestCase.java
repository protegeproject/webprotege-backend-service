package edu.stanford.bmir.protege.web.server.project;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.access.ActionId;
import edu.stanford.bmir.protege.web.server.dispatch.Action;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.project.GetAvailableProjectsWithPermissionAction;
import edu.stanford.bmir.protege.web.server.project.GetAvailableProjectsWithPermissionResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import static edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil.testSerialization;

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
