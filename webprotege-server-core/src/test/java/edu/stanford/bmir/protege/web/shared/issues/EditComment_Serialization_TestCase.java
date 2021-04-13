package edu.stanford.bmir.protege.web.shared.issues;

import edu.stanford.bmir.protege.web.MockingUtils;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-06
 */
public class EditComment_Serialization_TestCase {


    @Test
    public void shouldSerializeAction() throws IOException {
        var action = EditCommentAction.editComment(ProjectId.getNil(),
                                                   ThreadId.create(),
                                                   CommentId.create(),
                                                   "Body"); JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = EditCommentResult.create(Optional.empty(),
                                              MockingUtils.mockEventList());
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}
