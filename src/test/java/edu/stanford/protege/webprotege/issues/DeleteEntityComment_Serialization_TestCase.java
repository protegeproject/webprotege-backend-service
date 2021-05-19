package edu.stanford.protege.webprotege.issues;

import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import edu.stanford.protege.webprotege.project.ProjectId;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-06
 */
public class DeleteEntityComment_Serialization_TestCase {


    @Test
    public void shouldSerializeAction() throws IOException {
        var action = new DeleteEntityCommentAction(ProjectId.getNil(),
                                                   CommentId.create());
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = DeleteEntityCommentResult.create(CommentId.create(),
                                                      true);
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }



}
