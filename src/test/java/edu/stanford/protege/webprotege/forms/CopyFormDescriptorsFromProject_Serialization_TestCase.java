package edu.stanford.protege.webprotege.forms;

import com.google.common.collect.ImmutableList;

import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-06
 */
@JsonTest
public class CopyFormDescriptorsFromProject_Serialization_TestCase {

    @Autowired
    private JacksonTester<CopyFormDescriptorsAction> tester;


    @Test
    public void shouldSerializeAction() throws IOException {
        var toProjectId = ProjectId.generate();
        var fromProjectId = ProjectId.generate();
        var formId = FormId.generate();
        var action = new CopyFormDescriptorsAction(ChangeRequestId.generate(),
                                                   fromProjectId, toProjectId,
                                                   ImmutableList.of(formId));
        var json = tester.write(action);
        assertThat(json).hasJsonPathValue("fromProject", fromProjectId);
        assertThat(json).hasJsonPathValue("toProject", toProjectId);
        assertThat(json).hasJsonPathValue("formIds", formId);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var json = """
                {
                    "fromProject" : "11111111-1111-1111-1111-111111111111",
                    "toProject"   : "22222222-2222-2222-2222-222222222222",
                    "formIds"     : [
                        "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"
                    ]
                }
                """;
        var parsed = tester.parse(json);
        assertThat(parsed.getObject().fromProject()).isEqualTo(ProjectId.valueOf("11111111-1111-1111-1111-111111111111"));
        assertThat(parsed.getObject().toProject()).isEqualTo(ProjectId.valueOf("22222222-2222-2222-2222-222222222222"));
        assertThat(parsed.getObject().formIds()).contains(FormId.valueOf("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"));
    }
}
