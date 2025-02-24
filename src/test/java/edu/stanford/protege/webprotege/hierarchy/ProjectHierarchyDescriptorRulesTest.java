package edu.stanford.protege.webprotege.hierarchy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.protege.webprotege.authorization.ActionId;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.forms.FormId;
import edu.stanford.protege.webprotege.forms.field.FormRegionId;
import edu.stanford.protege.webprotege.jackson.WebProtegeJacksonApplication;
import edu.stanford.protege.webprotege.perspective.PerspectiveId;
import edu.stanford.protege.webprotege.ui.ViewId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

@JsonTest
@Import(WebProtegeJacksonApplication.class)
class ProjectHierarchyDescriptorRulesTest {

    @Autowired
    private ObjectMapper objectMapper;

    private JacksonTester<ProjectHierarchyDescriptorRules> jsonTester;

    @Autowired
    void setup(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    public void shouldThrowNpeIfProjectIdIsNull() {
        assertThrows(NullPointerException.class, () -> {
            new ProjectHierarchyDescriptorRules(null, List.of());
        });
    }

    @Test
    public void shouldThrowNpeIfRulesIsNull() {
        assertThrows(NullPointerException.class, () -> {
            new ProjectHierarchyDescriptorRules(ProjectId.generate(), null);
        });
    }

    @Test
    void testSerialization_TopLevelFields() throws Exception {
        var rule = new HierarchyDescriptorRule(
                PerspectiveId.get("123e4567-e89b-12d3-a456-426614174000"),
                ViewId.valueOf("123e4567-e89b-12d3-a456-426614174001"),
                java.util.Collections.singletonMap("key1", "value1"),
                FormId.valueOf("123e4567-e89b-12d3-a456-426614174002"),
                FormRegionId.valueOf("123e4567-e89b-12d3-a456-426614174003"),
                java.util.Set.of(
                        ActionId.valueOf("ACTIONX"),
                        ActionId.valueOf("ACTIONY")
                ),
                ClassHierarchyDescriptor.create()
        );

        var projectId = ProjectId.valueOf("123e4567-e89b-12d3-a456-426614174999");
        var projectRules = new ProjectHierarchyDescriptorRules(projectId, List.of(rule));

        JsonContent<ProjectHierarchyDescriptorRules> jsonContent = jsonTester.write(projectRules);

        assertThat(jsonContent).extractingJsonPathStringValue("$._id")
                .isEqualTo("123e4567-e89b-12d3-a456-426614174999");
        assertThat(jsonContent).extractingJsonPathArrayValue("$.rules").isNotEmpty();
        assertThat(jsonContent).extractingJsonPathStringValue("$.rules[0].requiredPerspectiveId")
                .isNotEmpty();
        assertThat(jsonContent).extractingJsonPathStringValue("$.rules[0].requiredViewId")
                .isNotEmpty();
        assertThat(jsonContent).extractingJsonPathValue("$.rules[0].requiredViewProperties")
                .isNotNull();
        assertThat(jsonContent).extractingJsonPathStringValue("$.rules[0].requiredFormId")
                .isNotEmpty();
        assertThat(jsonContent).extractingJsonPathStringValue("$.rules[0].requiredFormFieldId")
                .isNotEmpty();
        assertThat(jsonContent).extractingJsonPathValue("$.rules[0].requiredActions")
                .isNotNull();
        assertThat(jsonContent).extractingJsonPathStringValue("$.rules[0].hierarchyDescriptor.@type")
                .isEqualTo("ClassHierarchyDescriptor");
    }

    @Test
    void testDeserialization_TopLevelFields() throws Exception {
        var content = """
            {
              "_id": "123e4567-e89b-12d3-a456-426614174999",
              "rules": [
                {
                  "requiredPerspectiveId": "123e4567-e89b-12d3-a456-426614174000",
                  "requiredViewId": "123e4567-e89b-12d3-a456-426614174001",
                  "requiredViewProperties": {"key1": "value1"},
                  "requiredFormId": "123e4567-e89b-12d3-a456-426614174002",
                  "requiredFormFieldId": "123e4567-e89b-12d3-a456-426614174003",
                  "requiredActions": ["ACTIONX", "ACTIONY"],
                  "hierarchyDescriptor": {
                      "@type": "ClassHierarchyDescriptor",
                      "roots": [
                          {"@type": "Class", "iri": "http://www.w3.org/2002/07/owl#Thing"}
                      ]
                  }
                }
              ]
            }
            """;

        var projectRules = jsonTester.parseObject(content);

        assertThat(projectRules.projectId()).isEqualTo(ProjectId.valueOf("123e4567-e89b-12d3-a456-426614174999"));
        assertThat(projectRules.rules()).isNotEmpty();

        var rule = projectRules.rules().get(0);
        assertThat(rule.requiredPerspectiveId()).isNotNull();
        assertThat(rule.requiredViewId()).isNotNull();
        assertThat(rule.requiredViewProperties()).isNotNull();
        assertThat(rule.requiredFormId()).isNotNull();
        assertThat(rule.requiredFormFieldId()).isNotNull();
        assertThat(rule.requiredActions()).isNotEmpty();
        assertThat(rule.hierarchyDescriptor()).isNotNull();
    }
}
