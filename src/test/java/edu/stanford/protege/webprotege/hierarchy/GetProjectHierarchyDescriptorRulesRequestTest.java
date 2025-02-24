package edu.stanford.protege.webprotege.hierarchy;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.json.JacksonTester;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class GetProjectHierarchyDescriptorRulesRequestTest {

    private JacksonTester<GetProjectHierarchyDescriptorRulesRequest> jsonTester;

    private String projectIdUuid;

    private ProjectId projectId;

    @BeforeEach
    void setUp() {
        var objectMapper = new ObjectMapper();
        JacksonTester.initFields(this, objectMapper);
        projectIdUuid = UUID.randomUUID().toString();
        projectId = ProjectId.valueOf(projectIdUuid);
    }

    @Test
    void getChannel_shouldReturnCorrectChannelName() {
        var request = new GetProjectHierarchyDescriptorRulesRequest(projectId);
        assertThat(request.getChannel())
                .isEqualTo(GetProjectHierarchyDescriptorRulesRequest.CHANNEL);
    }

    @Test
    void constructor_shouldStoreProjectId() {
        var request = new GetProjectHierarchyDescriptorRulesRequest(projectId);
        assertThat(request.projectId())
                .isEqualTo(projectId);
    }

    @Test
    void serializeAndDeserialize_shouldWorkCorrectly() throws Exception {
        var request = new GetProjectHierarchyDescriptorRulesRequest(projectId);

        // Serialize the request to JSON
        var json = jsonTester.write(request);
        System.out.println(json.getJson());
        // Check JSON content
        assertThat(json).extractingJsonPathStringValue("projectId").isEqualTo(projectIdUuid);
        assertThat(json).extractingJsonPathStringValue("['@type']").isEqualTo("webprotege.hierarchies.GetProjectHierarchyDescriptorRules");

        // Deserialize the JSON back into an object
        var deserializedRequest = jsonTester.parseObject(json.getJson());

        // Verify the deserialized object
        assertThat(deserializedRequest.projectId())
                .isEqualTo(projectId);
        assertThat(deserializedRequest.getChannel())
                .isEqualTo(GetProjectHierarchyDescriptorRulesRequest.CHANNEL);
    }
}
