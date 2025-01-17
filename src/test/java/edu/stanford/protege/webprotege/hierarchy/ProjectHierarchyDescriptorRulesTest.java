package edu.stanford.protege.webprotege.hierarchy;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.protege.webprotege.authorization.ActionId;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.forms.FormId;
import edu.stanford.protege.webprotege.perspective.PerspectiveId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.json.JacksonTester;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProjectHierarchyDescriptorRulesTest {

    private JacksonTester<ProjectHierarchyDescriptorRules> jsonTester;

    @BeforeEach
    void setUp() {
        var objectMapper = new ObjectMapper();
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    void constructor_shouldThrowException_whenAnyParameterIsNull() {
        var projectId = ProjectId.valueOf(UUID.randomUUID().toString());
        var rule = new HierarchyDescriptorRule(
                Set.of(),
                Set.of(),
                Set.of(),
                ClassHierarchyDescriptor.create(Set.of())
        );

        assertThatThrownBy(() -> new ProjectHierarchyDescriptorRules(null, List.of(rule)))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("projectId cannot be null");

        assertThatThrownBy(() -> new ProjectHierarchyDescriptorRules(projectId, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("rules cannot be null");
    }

    @Test
    void serializeAndDeserialize_shouldWorkCorrectly() throws Exception {
        var projectIdUuid = UUID.randomUUID().toString();
        var projectId = ProjectId.valueOf(projectIdUuid);
        var perspectiveUuid = UUID.randomUUID().toString();
        var formIdUuid = UUID.randomUUID().toString();
        var rule = new HierarchyDescriptorRule(
                Set.of(ActionId.valueOf("action1")),
                Set.of(PerspectiveId.get(perspectiveUuid)),
                Set.of(FormId.get(formIdUuid)),
                ClassHierarchyDescriptor.create(Set.of())
        );
        var rules = new ProjectHierarchyDescriptorRules(projectId, List.of(rule));

        var json = jsonTester.write(rules);
        assertThat(json).extractingJsonPathStringValue("projectId").isEqualTo(projectIdUuid);
        assertThat(json).extractingJsonPathArrayValue("rules").isNotEmpty();

        var deserializedRules = jsonTester.parseObject(json.getJson());
        assertThat(deserializedRules).isEqualTo(rules);
    }

    @Test
    void createWithEmptyRules_shouldReturnInstance() {
        var projectId = ProjectId.valueOf(UUID.randomUUID().toString());
        var rules = new ProjectHierarchyDescriptorRules(projectId, List.of());

        assertThat(rules.projectId()).isEqualTo(projectId);
        assertThat(rules.rules()).isEmpty();
    }
}
