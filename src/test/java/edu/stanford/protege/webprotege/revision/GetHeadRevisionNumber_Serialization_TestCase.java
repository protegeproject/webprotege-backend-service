package edu.stanford.protege.webprotege.revision;

import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import edu.stanford.protege.webprotege.project.ProjectId;
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