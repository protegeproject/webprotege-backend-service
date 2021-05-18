package edu.stanford.bmir.protege.web.server.frame;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.MockingUtils;
import edu.stanford.bmir.protege.web.server.dispatch.Action;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.server.project.ProjectId;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-06
 */
public class CheckManchesterSyntaxFrame_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = CheckManchesterSyntaxFrameAction.create(ProjectId.getNil(),
                                                             MockingUtils.mockOWLClass(),
                                                             "From",
                                                             "To",
                                                             ImmutableSet.of());

        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = CheckManchesterSyntaxFrameResult.create(ManchesterSyntaxFrameParseResult.CHANGED, null);
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}
