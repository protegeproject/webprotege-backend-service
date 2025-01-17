package edu.stanford.protege.webprotege.hierarchy;


import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.protege.webprotege.authorization.ActionId;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.forms.FormId;
import edu.stanford.protege.webprotege.perspective.PerspectiveId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.json.JacksonTester;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HierarchyDescriptorRuleTest {

    private JacksonTester<HierarchyDescriptorRule> jsonTester;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    void shouldThrowException_whenAnyParameterIsNull() {
        var hierarchyDescriptor = ClassHierarchyDescriptor.create(Set.of());
        assertThatThrownBy(() -> new HierarchyDescriptorRule(null, Set.of(), Set.of(), hierarchyDescriptor))
                .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> new HierarchyDescriptorRule(Set.of(), null, Set.of(), hierarchyDescriptor))
                .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> new HierarchyDescriptorRule(Set.of(), Set.of(), null, hierarchyDescriptor))
                .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> new HierarchyDescriptorRule(Set.of(), Set.of(), Set.of(), null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void create_shouldReturnInstanceWithEmptySets() {
        var hierarchyDescriptor = ClassHierarchyDescriptor.create(Set.of());
        var projectId = ProjectId.generate();
        var rule = HierarchyDescriptorRule.create(hierarchyDescriptor);

        assertThat(rule.matchedActions()).isEmpty();
        assertThat(rule.matchedPerspectiveIds()).isEmpty();
        assertThat(rule.matchedFormIds()).isEmpty();
        assertThat(rule.hierarchyDescriptor()).isEqualTo(hierarchyDescriptor);
    }

    @Test
    void serializeAndDeserialize_shouldWorkCorrectly() throws Exception {
        var hierarchyDescriptor = ClassHierarchyDescriptor.create(Set.of());
        var perspectiveUuid = UUID.randomUUID().toString();
        var formUuid = UUID.randomUUID().toString();
        var rule = new HierarchyDescriptorRule(
                Set.of(ActionId.valueOf("action1")),
                Set.of(PerspectiveId.get(perspectiveUuid)),
                Set.of(FormId.get(formUuid)),
                hierarchyDescriptor
        );

        var content = jsonTester.write(rule);
        assertThat(content).extractingJsonPathArrayValue("matchedActions").contains("action1");
        assertThat(content).extractingJsonPathArrayValue("matchedPerspectiveIds").contains(perspectiveUuid);
        assertThat(content).extractingJsonPathArrayValue("matchedFormIds").contains(formUuid);
        System.out.println(content.getJson());

        var deserializedRule = jsonTester.parseObject(content.getJson());
        assertThat(deserializedRule).isEqualTo(rule);
    }
}