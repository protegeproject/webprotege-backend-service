package edu.stanford.protege.webprotege.forms;


import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.junit.jupiter.api.Test;


import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-06
 */
public class DeleteForm_Serialization_TestCase {


    @Test
    public void shouldSerializeAction() throws IOException {
        var action = new DeleteFormAction(ChangeRequestId.generate(),
                                          ProjectId.generate(),
                                          FormId.generate());

    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = new DeleteFormResult();
    }
}
