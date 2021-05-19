package edu.stanford.protege.webprotege.form;

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
public class DeleteForm_Serialization_TestCase {


    @Test
    public void shouldSerializeAction() throws IOException {
        var action = DeleteFormAction.get(ProjectId.getNil(),
                                          FormId.generate());
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = DeleteFormResult.create();
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}
