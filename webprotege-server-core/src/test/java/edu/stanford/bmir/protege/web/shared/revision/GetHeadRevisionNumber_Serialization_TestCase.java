package edu.stanford.bmir.protege.web.shared.revision;

import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Test;

import java.io.IOException;


import static edu.stanford.bmir.protege.web.MockingUtils.*;

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