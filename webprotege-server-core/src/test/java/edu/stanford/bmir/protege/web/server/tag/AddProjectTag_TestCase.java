package edu.stanford.bmir.protege.web.server.tag;

import edu.stanford.bmir.protege.web.server.color.Color;
import edu.stanford.bmir.protege.web.server.dispatch.Action;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.tag.AddProjectTagAction;
import edu.stanford.bmir.protege.web.server.tag.AddProjectTagResult;
import edu.stanford.bmir.protege.web.server.tag.Tag;
import edu.stanford.bmir.protege.web.server.tag.TagId;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.server.project.ProjectId;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-05
 */
public class AddProjectTag_TestCase {

    @Test
    public void shouldSerializeAddProjectTagAction() throws IOException {
        var action = AddProjectTagAction.create(ProjectId.getNil(),
                                                "TheLabel",
                                                "TheDescription",
                                                Color.getWhite(),
                                                Color.get(240, 230, 50));
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeAddProjectTagResult() throws IOException {
        var result = AddProjectTagResult.create(
                Tag.get(TagId.createTagId(),
                        ProjectId.getNil(),
                        "TheLabel",
                        "TheDescription",
                        Color.getWhite(),
                        Color.getWhite(),
                        Collections.emptyList())
        );
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}
