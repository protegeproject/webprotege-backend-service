package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.authorization.ActionId;
import edu.stanford.protege.webprotege.ui.DisplayContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class HierarchyDescriptorRuleDisplayContextMatcher {

    private static final Logger logger = LoggerFactory.getLogger(HierarchyDescriptorRuleDisplayContextMatcher.class);

    public boolean matches(HierarchyDescriptorRule rule,
                           DisplayContext displayContext,
                           Set<ActionId> actions) {
        logger.info("Testing if {} matches {} with actions {}", rule, displayContext, actions);
        return matchesPerspective(rule, displayContext)
                && matchesView(rule, displayContext)
                && matchesViewProperties(rule, displayContext)
                && matchesForm(rule, displayContext)
                && matchesFormField(rule, displayContext)
                && matchesActions(rule, actions);
    }

    private boolean matchesPerspective(HierarchyDescriptorRule rule, DisplayContext displayContext) {
        if (rule.requiredPerspectiveId() != null
                && !rule.requiredPerspectiveId().equals(displayContext.perspectiveId())) {
            logger.debug("Rule {} does not match perspective id {}", rule, displayContext.perspectiveId());
            return false;
        }
        return true;
    }

    private boolean matchesView(HierarchyDescriptorRule rule, DisplayContext displayContext) {
        if (rule.requiredViewId() != null
                && !rule.requiredViewId().equals(displayContext.viewId())) {
            logger.debug("Rule {} does not match view id {}", rule, displayContext.viewId());
            return false;
        }
        return true;
    }

    private boolean matchesViewProperties(HierarchyDescriptorRule rule, DisplayContext displayContext) {
        boolean propertiesMatch = rule.requiredViewProperties()
                .entrySet()
                .stream()
                .allMatch(e -> e.getValue().equals(displayContext.viewProperties().get(e.getKey())));
        if (!propertiesMatch) {
            logger.debug("Rule {} does not match view properties {}", rule, displayContext.viewProperties());
            return false;
        }
        return true;
    }

    private boolean matchesForm(HierarchyDescriptorRule rule, DisplayContext displayContext) {
        if (rule.requiredFormId() != null
                && !displayContext.formIds().contains(rule.requiredFormId())) {
            logger.debug("Rule {} does not match form id {}", rule, displayContext.formIds());
            return false;
        }
        return true;
    }

    private boolean matchesFormField(HierarchyDescriptorRule rule, DisplayContext displayContext) {
        if (rule.requiredFormFieldId() != null
                && !rule.requiredFormFieldId().equals(displayContext.formFieldId())) {
            logger.debug("Rule {} does not match form field id {}", rule, displayContext.formFieldId());
            return false;
        }
        return true;
    }

    private boolean matchesActions(HierarchyDescriptorRule rule, Set<ActionId> actions) {
        if (!actions.containsAll(rule.requiredActions())) {
            logger.debug("Rule {} does not match actions {}", rule, actions);
            return false;
        }
        return true;
    }
}
