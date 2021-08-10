package edu.stanford.protege.webprotege.project;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.authorization.api.ActionId;
import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.dispatch.Result;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import static edu.stanford.protege.webprotege.match.JsonSerializationTestUtil.testSerialization;

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
