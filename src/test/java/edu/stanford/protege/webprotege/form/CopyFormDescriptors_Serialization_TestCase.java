package edu.stanford.protege.webprotege.form;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-06
 */
public class CopyFormDescriptors_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = CopyFormDescriptorsAction.create(ProjectId.getNil(),
                                                      ProjectId.getNil(),
                                                      ImmutableList.of());
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = CopyFormDescriptorsResult.create(ImmutableList.of());
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}