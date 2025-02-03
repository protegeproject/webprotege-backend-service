package edu.stanford.protege.webprotege.hierarchy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.jackson.WebProtegeJacksonApplication;
import edu.stanford.protege.webprotege.perspective.PerspectiveId;
import edu.stanford.protege.webprotege.ui.DisplayContext;
import edu.stanford.protege.webprotege.ui.ViewId;
import edu.stanford.protege.webprotege.ui.ViewNodeId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Map;

@JsonTest
@Import(WebProtegeJacksonApplication.class)
class GetHierarchyDescriptorRequestTest {

    @Autowired
    private ObjectMapper objectMapper;

    private JacksonTester<GetHierarchyDescriptorRequest> jsonTester;

    @Autowired
    void setup(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    void testSerialization_TopLevelFields() throws Exception {
        var projectId = ProjectId.valueOf("123e4567-e89b-12d3-a456-426614174888");
        var displayContext = new DisplayContext(
                projectId,
                PerspectiveId.generate(),
                ViewId.generate(),
                ViewNodeId.generate(),
                Map.of(),
                List.of(),
                null,
                List.of()
        );
        var request = new GetHierarchyDescriptorRequest(projectId, displayContext);

        JsonContent<GetHierarchyDescriptorRequest> jsonContent = jsonTester.write(request);

        assertThat(jsonContent).extractingJsonPathStringValue("$.projectId")
                .isEqualTo("123e4567-e89b-12d3-a456-426614174888");
        assertThat(jsonContent).extractingJsonPathValue("$.displayContext").isNotNull();
        assertThat(request.getChannel()).isEqualTo("webprotege.hierarchies.GetHierarchyDescriptor");
    }

    @Test
    void testDeserialization_TopLevelFields() throws Exception {
        var content = """
            {
              "projectId": "123e4567-e89b-12d3-a456-426614174888",
              "displayContext": {
                    "projectId": "892b4506-726c-49be-90b1-76e95318291c",
                    "perspectiveId": "ecd2b5b0-c691-4302-ac2a-0fc3215a8c31",
                    "viewId": "b1791234-a18f-4274-b972-4c83b2359832",
                    "viewNodeId": "e3cdc077-be52-4423-91c6-c7ede91eaa53",
                    "viewProperties": {"key": "value"},
                    "formIds": ["cde42f26-3ef5-4043-9203-38643f20d17e"],
                    "formFieldId": "963b1b0f-fa2f-4298-b36d-4660eab83d79",
                    "selectedPaths": [[{"@type" : "Class", "iri":"http://example.org/Class1"}]]
                }
            }
            """;

        var request = jsonTester.parseObject(content);

        assertThat(request.projectId())
                .isEqualTo(ProjectId.valueOf("123e4567-e89b-12d3-a456-426614174888"));
        assertThat(request.displayContext())
                .isNotNull();
        assertThat(request.getChannel())
                .isEqualTo("webprotege.hierarchies.GetHierarchyDescriptor");
    }

    @Test
    void shouldThrowNpeIfProjectIdIsNull() {
        var displayContext = mock(DisplayContext.class);
        assertThrows(NullPointerException.class, () -> new GetHierarchyDescriptorRequest(null, displayContext));
    }

    @Test
    void shouldThrowNpeIfDisplayContextIsNull() {
        assertThrows(NullPointerException.class, () -> {
            new GetHierarchyDescriptorRequest(ProjectId.generate(), null);
        });
    }
}
