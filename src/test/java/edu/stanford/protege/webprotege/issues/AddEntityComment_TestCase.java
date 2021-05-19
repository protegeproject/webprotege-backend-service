package edu.stanford.protege.webprotege.issues;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.event.EventList;
import edu.stanford.protege.webprotege.event.EventTag;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import edu.stanford.protege.webprotege.project.ProjectId;
import edu.stanford.protege.webprotege.user.UserId;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-05
 */
public class AddEntityComment_TestCase {

    private static final String THE_COMMENT = "The comment";

    private final ProjectId projectId = ProjectId.getNil();

    private final ThreadId threadId = ThreadId.create();

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = AddEntityCommentAction.addComment(projectId, threadId, THE_COMMENT);
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = AddEntityCommentResult.create(projectId,
                                                   threadId,
                                                   new Comment(CommentId.create(), UserId.getUserId("TheUser"),
                                                               1000, Optional.empty(),
                                                               THE_COMMENT,
                                                               THE_COMMENT),
                                                   THE_COMMENT,
                                                   EventList.create(EventTag.get(3),
                                                       ImmutableList.of(),
                                                       EventTag.get(3)));
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}
