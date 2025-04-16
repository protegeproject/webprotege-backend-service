package edu.stanford.protege.webprotege.hierarchy;

import static org.assertj.core.api.Assertions.assertThat;

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
 * Comprehensive unit tests for {@link HierarchyDescriptorRule}.
 */
@JsonTest
@Import(WebProtegeJacksonApplication.class)
class HierarchyDescriptorRuleTest {

    private static final String PERSPECTIVE_UUID = "123e4567-e89b-12d3-a456-426614174000";

    private static final String VIEW_UUID         = "123e4567-e89b-12d3-a456-426614174001";

    private static final String FORM_UUID         = "123e4567-e89b-12d3-a456-426614174002";

    private static final String FORM_FIELD_UUID   = "123e4567-e89b-12d3-a456-426614174003";

    private static final String ACTIONX_STRING    = "ACTIONX";

    private static final String ACTIONY_STRING    = "ACTIONY";

    private JacksonTester<HierarchyDescriptorRule> json;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        // Initialize JacksonTester with the ObjectMapper.
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    void testSerialization() throws Exception {
        // Create sample domain objects using their factory methods.
        var perspectiveId = PerspectiveId.get(PERSPECTIVE_UUID);
        var viewId = ViewId.valueOf(VIEW_UUID);
        var viewProperties = Map.of("key1", "value1");
        var formId = FormId.valueOf(FORM_UUID);
        var formRegionId = FormRegionId.valueOf(FORM_FIELD_UUID);
        // Create ActionIds as regular strings.
        var actions = Set.<Capability>of(
                BasicCapability.valueOf(ACTIONX_STRING),
                BasicCapability.valueOf(ACTIONY_STRING)
        );
        // Use ClassHierarchyDescriptor.create() (no parameters) to obtain a default hierarchy descriptor.
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

        var jsonContent = json.write(rule);
        assertThat(jsonContent).extractingJsonPathStringValue("@.requiredPerspectiveId")
                .isEqualTo(PERSPECTIVE_UUID);
        assertThat(jsonContent).extractingJsonPathStringValue("@.requiredViewId")
                .isEqualTo(VIEW_UUID);
        assertThat(jsonContent).extractingJsonPathMapValue("@.requiredViewProperties")
                .containsEntry("key1", "value1");
        assertThat(jsonContent).extractingJsonPathStringValue("@.requiredFormId")
                .isEqualTo(FORM_UUID);
        assertThat(jsonContent).extractingJsonPathStringValue("@.requiredFormFieldId")
                .isEqualTo(FORM_FIELD_UUID);
        assertThat(jsonContent).extractingJsonPathArrayValue("@.requiredActions").hasSize(2);
        assertThat(jsonContent).extractingJsonPathMapValue("@.hierarchyDescriptor").isNotNull();
    }

    @Test
    void testDeserialization() throws Exception {
        // Provide JSON where all identifier fields are represented correctly.
        var content = """
            {
              "requiredPerspectiveId": "123e4567-e89b-12d3-a456-426614174000",
              "requiredViewId": "123e4567-e89b-12d3-a456-426614174001",
              "requiredViewProperties": {"key1": "value1"},
              "requiredFormId": "123e4567-e89b-12d3-a456-426614174002",
              "requiredFormFieldId": "123e4567-e89b-12d3-a456-426614174003",
              "requiredActions": [{"@type":"BasicCapability","id":"ACTIONX"}, {"@type":"BasicCapability", "id":"ACTIONY"}],
              "hierarchyDescriptor": { "@type": "ClassHierarchyDescriptor", "roots": [{"@type":"Class", "iri":"http://www.w3.org/2002/07/owl#Thing"}]}
            }
            """;

        var rule = json.parseObject(content);

        assertThat(rule.requiredPerspectiveId()).isEqualTo(PerspectiveId.get(PERSPECTIVE_UUID));
        assertThat(rule.requiredViewId()).isEqualTo(ViewId.valueOf(VIEW_UUID));
        assertThat(rule.requiredViewProperties()).containsExactlyEntriesOf(Map.of("key1", "value1"));
        assertThat(rule.requiredFormId()).isEqualTo(FormId.valueOf(FORM_UUID));
        assertThat(rule.requiredFormFieldId()).isEqualTo(FormRegionId.valueOf(FORM_FIELD_UUID));
        assertThat(rule.requiredActions()).containsExactlyInAnyOrder(
                BasicCapability.valueOf(ACTIONX_STRING),
                BasicCapability.valueOf(ACTIONY_STRING)
        );
        assertThat(rule.hierarchyDescriptor()).isEqualTo(ClassHierarchyDescriptor.create());
    }

    @Test
    void testCreateMethod() {
        // Test that the factory method creates a rule with empty/null matching criteria.
        var hd = ClassHierarchyDescriptor.create();
        var rule = HierarchyDescriptorRule.create(hd);

        assertThat(rule.requiredPerspectiveId()).isNull();
        assertThat(rule.requiredViewId()).isNull();
        assertThat(rule.requiredViewProperties()).isEmpty();
        assertThat(rule.requiredFormId()).isNull();
        assertThat(rule.requiredFormFieldId()).isNull();
        assertThat(rule.requiredActions()).isEmpty();
        assertThat(rule.hierarchyDescriptor()).isEqualTo(hd);
    }
}
