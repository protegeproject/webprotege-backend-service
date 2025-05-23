package edu.stanford.protege.webprotege.hierarchy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.protege.webprotege.authorization.BasicCapability;
import edu.stanford.protege.webprotege.authorization.Capability;
import edu.stanford.protege.webprotege.forms.FormId;
import edu.stanford.protege.webprotege.forms.field.FormRegionId;
import edu.stanford.protege.webprotege.jackson.WebProtegeJacksonApplication;
import edu.stanford.protege.webprotege.perspective.PerspectiveId;
import edu.stanford.protege.webprotege.ui.ViewId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.Import;

/**
 * Comprehensive tests for {@link GetProjectHierarchyDescriptorRulesResponse}.
 */
@JsonTest
@Import(WebProtegeJacksonApplication.class)
class GetProjectHierarchyDescriptorRulesResponseTest {

    private JacksonTester<GetProjectHierarchyDescriptorRulesResponse> json;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    public void shouldThrowNpe() {
        assertThrows(NullPointerException.class, () -> {
           new GetProjectHierarchyDescriptorRulesResponse(null);
        });
    }

    @Test
    void testSerialization() throws Exception {
        // Create a sample HierarchyDescriptorRule.
        var perspectiveId = PerspectiveId.get("123e4567-e89b-12d3-a456-426614174000");
        var viewId = ViewId.valueOf("123e4567-e89b-12d3-a456-426614174001");
        var viewProperties = Map.of("key1", "value1");
        var formId = FormId.valueOf("123e4567-e89b-12d3-a456-426614174002");
        var formRegionId = FormRegionId.valueOf("123e4567-e89b-12d3-a456-426614174003");
        var actions = Set.<Capability>of(
                BasicCapability.valueOf("ACTIONX"),
                BasicCapability.valueOf("ACTIONY")
        );
        var hd = ClassHierarchyDescriptor.create();

        var rule = new HierarchyDescriptorRule(
                perspectiveId,
                viewId,
                viewProperties,
                formId,
                formRegionId,
                actions,
                hd
        );

        // Create a response containing a single rule.
        var response = new GetProjectHierarchyDescriptorRulesResponse(List.of(rule));

        var jsonContent = json.write(response);
        assertThat(jsonContent).extractingJsonPathArrayValue("$.rules").hasSize(1);
    }

    @Test
    void testDeserialization() throws Exception {
        var content = """
            {
              "rules": [
                {
                  "requiredPerspectiveId": "123e4567-e89b-12d3-a456-426614174000",
                  "requiredViewId": "123e4567-e89b-12d3-a456-426614174001",
                  "requiredViewProperties": {"key1": "value1"},
                  "requiredFormId": "123e4567-e89b-12d3-a456-426614174002",
                  "requiredFormFieldId": "123e4567-e89b-12d3-a456-426614174003",
                  "requiredCapabilities": [{"@type":"BasicCapability","id":"ACTIONX"}, {"@type":"BasicCapability", "id":"ACTIONY"}],
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

        // Deserialize the JSON into a response object.
        var response = json.parseObject(content);

        // Verify that the response contains exactly one rule.
        assertThat(response.rules()).hasSize(1);
        var rule = response.rules().get(0);
        assertThat(rule.requiredPerspectiveId())
                .isEqualTo(PerspectiveId.get("123e4567-e89b-12d3-a456-426614174000"));
        assertThat(rule.requiredViewId())
                .isEqualTo(ViewId.valueOf("123e4567-e89b-12d3-a456-426614174001"));
        assertThat(rule.requiredViewProperties())
                .containsExactlyEntriesOf(Map.of("key1", "value1"));
        assertThat(rule.requiredFormId())
                .isEqualTo(FormId.valueOf("123e4567-e89b-12d3-a456-426614174002"));
        assertThat(rule.requiredFormFieldId())
                .isEqualTo(FormRegionId.valueOf("123e4567-e89b-12d3-a456-426614174003"));
        assertThat(rule.requiredCapabilities())
                .containsExactlyInAnyOrder(
                        BasicCapability.valueOf("ACTIONX"),
                        BasicCapability.valueOf("ACTIONY")
                );
    }
}