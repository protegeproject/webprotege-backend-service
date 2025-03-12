package edu.stanford.protege.webprotege.hierarchy;

import static org.assertj.core.api.Assertions.assertThat;

import edu.stanford.protege.webprotege.authorization.BasicCapability;
import edu.stanford.protege.webprotege.authorization.Capability;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.forms.FormId;
import edu.stanford.protege.webprotege.forms.field.FormRegionId;
import edu.stanford.protege.webprotege.perspective.PerspectiveId;
import edu.stanford.protege.webprotege.ui.DisplayContext;
import edu.stanford.protege.webprotege.ui.ViewId;
import edu.stanford.protege.webprotege.ui.ViewNodeId;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

class HierarchyDescriptorRuleDisplayContextMatcherTest {

    public static final PerspectiveId MATCHING_PERSPECTIVE_ID = PerspectiveId.get("123e4567-e89b-12d3-a456-426614174001");
    public static final ViewId MATCHING_VIEW_ID = ViewId.valueOf("view1");
    public static final Map<String, String> MATCHING_VIEW_PROPERTIES = Map.of("key", "value");
    public static final FormId MATCHING_FORM_ID = FormId.valueOf("123e4567-e89b-12d3-a456-426614174010");
    public static final FormRegionId MATCHING_FORM_FIELD_ID = FormRegionId.valueOf("123e4567-e89b-12d3-a456-426614174011");
    public static final Set<Capability> MATCHING_ACTION_IDS = Set.of(BasicCapability.valueOf("ACTIONX"));
    private final HierarchyDescriptorRuleDisplayContextMatcher matcher =
            new HierarchyDescriptorRuleDisplayContextMatcher();

    // Helper to create a rule with specific matching criteria.
    private HierarchyDescriptorRule createRule(
            PerspectiveId requiredPerspectiveId,
            ViewId requiredViewId,
            Map<String, String> requiredViewProperties,
            FormId requiredFormId,
            FormRegionId requiredFormFieldId,
            Set<Capability> requiredActions) {
        return new HierarchyDescriptorRule(
                requiredPerspectiveId,
                requiredViewId,
                requiredViewProperties,
                requiredFormId,
                requiredFormFieldId,
                requiredActions,
                ClassHierarchyDescriptor.create() // using default hierarchy descriptor
        );
    }

    // Helper to create a DisplayContext.
    private DisplayContext createDisplayContext(
            PerspectiveId perspectiveId,
            ViewId viewId,
            Map<String, String> viewProperties,
            List<FormId> formIds,
            FormRegionId formFieldId) {
        var viewNodeId = ViewNodeId.valueOf("vn1");
        return new DisplayContext(
                ProjectId.valueOf("123e4567-e89b-12d3-a456-426614174aaa"),
                perspectiveId,
                viewId,
                viewNodeId,
                viewProperties,
                formIds,
                formFieldId,
                Collections.emptyList()
        );
    }

    @Test
    void testMatches_AllCriteriaMet() {
        var perspectiveId = MATCHING_PERSPECTIVE_ID;
        var viewId = MATCHING_VIEW_ID;
        var viewProperties = MATCHING_VIEW_PROPERTIES;
        var formId = MATCHING_FORM_ID;
        var formFieldId = MATCHING_FORM_FIELD_ID; 
        var ruleActions = MATCHING_ACTION_IDS;

        var rule = createRule(perspectiveId, viewId, viewProperties, formId, formFieldId, ruleActions);
        var displayContext = createDisplayContext(perspectiveId, viewId, viewProperties, List.of(formId), formFieldId);
        var providedActions = Set.<Capability>of(BasicCapability.valueOf("ACTIONX"), BasicCapability.valueOf("EXTRA"));

        boolean result = matcher.matches(rule, displayContext, providedActions);
        assertThat(result).isTrue();
    }

    @Test
    void testMatches_PerspectiveMismatch() {
        var rulePerspective = MATCHING_PERSPECTIVE_ID;
        var displayPerspective = PerspectiveId.get("123e4567-e89b-12d3-a456-426614174002"); // mismatch
        var viewId = MATCHING_VIEW_ID;
        var viewProperties = MATCHING_VIEW_PROPERTIES;
        var formId = MATCHING_FORM_ID;
        var formFieldId = MATCHING_FORM_FIELD_ID;
        var ruleActions = Set.<Capability>of(BasicCapability.valueOf("ACTIONX"));

        var rule = createRule(rulePerspective, viewId, viewProperties, formId, formFieldId, ruleActions);
        var displayContext = createDisplayContext(displayPerspective, viewId, viewProperties, List.of(formId), formFieldId);
        var providedActions = Set.<Capability>of(BasicCapability.valueOf("ACTIONX"));

        boolean result = matcher.matches(rule, displayContext, providedActions);
        assertThat(result).isFalse();
    }

    @Test
    void testMatches_ViewIdMismatch() {
        var perspectiveId = MATCHING_PERSPECTIVE_ID;
        var ruleViewId = MATCHING_VIEW_ID;
        var displayViewId = ViewId.valueOf("view2"); // mismatch
        var viewProperties = MATCHING_VIEW_PROPERTIES;
        var formId = MATCHING_FORM_ID;
        var formFieldId = MATCHING_FORM_FIELD_ID;
        var ruleActions = Set.<Capability>of(BasicCapability.valueOf("ACTIONX"));

        var rule = createRule(perspectiveId, ruleViewId, viewProperties, formId, formFieldId, ruleActions);
        var displayContext = createDisplayContext(perspectiveId, displayViewId, viewProperties, List.of(formId), formFieldId);
        var providedActions = Set.<Capability>of(BasicCapability.valueOf("ACTIONX"));

        boolean result = matcher.matches(rule, displayContext, providedActions);
        assertThat(result).isFalse();
    }

    @Test
    void testMatches_ViewPropertiesMismatch() {
        var perspectiveId = MATCHING_PERSPECTIVE_ID;
        var viewId = MATCHING_VIEW_ID;
        var ruleViewProperties = MATCHING_VIEW_PROPERTIES;
        var displayViewProperties = Map.of("key", "different"); // mismatch
        var formId = MATCHING_FORM_ID;
        var formFieldId = MATCHING_FORM_FIELD_ID;
        var ruleActions = Set.<Capability>of(BasicCapability.valueOf("ACTIONX"));

        var rule = createRule(perspectiveId, viewId, ruleViewProperties, formId, formFieldId, ruleActions);
        var displayContext = createDisplayContext(perspectiveId, viewId, displayViewProperties, List.of(formId), formFieldId);
        var providedActions = Set.<Capability>of(BasicCapability.valueOf("ACTIONX"));

        boolean result = matcher.matches(rule, displayContext, providedActions);
        assertThat(result).isFalse();
    }

    @Test
    void testMatches_FormIdMismatch() {
        var perspectiveId = MATCHING_PERSPECTIVE_ID;
        var viewId = MATCHING_VIEW_ID;
        var viewProperties = MATCHING_VIEW_PROPERTIES;
        var ruleFormId = MATCHING_FORM_ID;
        var displayFormId = FormId.valueOf("123e4567-e89b-12d3-a456-426614174012"); // mismatch
        var formFieldId = MATCHING_FORM_FIELD_ID;
        var ruleActions = Set.<Capability>of(BasicCapability.valueOf("ACTIONX"));

        var rule = createRule(perspectiveId, viewId, viewProperties, ruleFormId, formFieldId, ruleActions);
        var displayContext = createDisplayContext(perspectiveId, viewId, viewProperties, List.of(displayFormId), formFieldId);
        var providedActions = Set.<Capability>of(BasicCapability.valueOf("ACTIONX"));

        boolean result = matcher.matches(rule, displayContext, providedActions);
        assertThat(result).isFalse();
    }

    @Test
    void testMatches_FormFieldIdMismatch() {
        var perspectiveId = MATCHING_PERSPECTIVE_ID;
        var viewId = MATCHING_VIEW_ID;
        var viewProperties = MATCHING_VIEW_PROPERTIES;
        var formId = MATCHING_FORM_ID;
        var ruleFormFieldId = MATCHING_FORM_FIELD_ID;
        var displayFormFieldId = FormRegionId.valueOf("123e4567-e89b-12d3-a456-426614174013"); // mismatch
        var ruleActions = Set.<Capability>of(BasicCapability.valueOf("ACTIONX"));

        var rule = createRule(perspectiveId, viewId, viewProperties, formId, ruleFormFieldId, ruleActions);
        var displayContext = createDisplayContext(perspectiveId, viewId, viewProperties, List.of(formId), displayFormFieldId);
        var providedActions = Set.<Capability>of(BasicCapability.valueOf("ACTIONX"));

        boolean result = matcher.matches(rule, displayContext, providedActions);
        assertThat(result).isFalse();
    }

    @Test
    void testMatches_ActionsMismatch() {
        var perspectiveId = MATCHING_PERSPECTIVE_ID;
        var viewId = MATCHING_VIEW_ID;
        var viewProperties = MATCHING_VIEW_PROPERTIES;
        var formId = MATCHING_FORM_ID;
        var formFieldId = MATCHING_FORM_FIELD_ID;
        var ruleActions = MATCHING_ACTION_IDS;
        var rule = createRule(perspectiveId, viewId, viewProperties, formId, formFieldId, ruleActions);
        var displayContext = createDisplayContext(perspectiveId, viewId, viewProperties, List.of(formId), formFieldId);

        // Provided actions do not include ACTIONX.
        var providedActions = Set.<Capability>of(BasicCapability.valueOf("ACTIONQ"));
        boolean result = matcher.matches(rule, displayContext, providedActions);
        assertThat(result).isFalse();
    }

    @Test
    void testMatches_NullOptionalRuleFieldsIgnored() {
        // Create a rule with optional criteria null/empty.
        var rule = HierarchyDescriptorRule.create(ClassHierarchyDescriptor.create());
        var perspectiveId = MATCHING_PERSPECTIVE_ID;
        var viewId = MATCHING_VIEW_ID;
        var viewProperties = MATCHING_VIEW_PROPERTIES;
        var formId = MATCHING_FORM_ID;
        var formFieldId = MATCHING_FORM_FIELD_ID;
        var displayContext = createDisplayContext(perspectiveId, viewId, viewProperties, List.of(formId), formFieldId);
        var providedActions = Set.<Capability>of(BasicCapability.valueOf("ACTIONX"));

        boolean result = matcher.matches(rule, displayContext, providedActions);
        assertThat(result).isTrue();
    }
}
