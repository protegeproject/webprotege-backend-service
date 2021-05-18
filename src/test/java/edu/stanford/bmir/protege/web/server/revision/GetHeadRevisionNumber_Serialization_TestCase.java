package edu.stanford.bmir.protege.web.server.revision;

import edu.stanford.bmir.protege.web.server.dispatch.Action;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.revision.GetHeadRevisionNumberAction;
import edu.stanford.bmir.protege.web.server.revision.GetHeadRevisionNumberResult;
import edu.stanford.bmir.protege.web.server.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.server.project.ProjectId;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class GetHeadRevisionNumber_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = GetHeadRevisionNumberAction.create(ProjectId.getNil());
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = GetHeadRevisionNumberResult.create(RevisionNumber.getHeadRevisionNumber());
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}