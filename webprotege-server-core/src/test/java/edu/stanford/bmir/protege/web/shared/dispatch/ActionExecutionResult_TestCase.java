package edu.stanford.bmir.protege.web.shared.dispatch;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.color.Color;
import edu.stanford.bmir.protege.web.shared.issues.AddEntityCommentResult;
import edu.stanford.bmir.protege.web.shared.issues.Comment;
import edu.stanford.bmir.protege.web.shared.issues.ThreadId;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.tag.AddProjectTagAction;
import edu.stanford.bmir.protege.web.shared.tag.AddProjectTagResult;
import edu.stanford.bmir.protege.web.shared.tag.Tag;
import edu.stanford.bmir.protege.web.shared.tag.TagId;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-05
 */
public class ActionExecutionResult_TestCase {

    @Test
    public void shouldSerializeResult() throws IOException {
        var nestedResult = AddProjectTagResult.create(Tag.get(
                TagId.createTagId(),
                ProjectId.getNil(),
                "Tag",
                "Desc",
                Color.getWhite(),
                Color.getWhite(),
                ImmutableList.of()
        ));
        var result = ActionExecutionResult.get(DispatchServiceResultContainer.create(nestedResult));
        JsonSerializationTestUtil.testSerialization(result, ActionExecutionResult.class);
    }
}
