package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.authorization.Capability;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.ui.DisplayContext;

import java.util.Optional;
import java.util.Set;

public class HierarchyDescriptorRuleSelector {

    private final HierarchyDescriptorRulesRepository repository;

    private final HierarchyDescriptorRuleDisplayContextMatcher matcher;

    public HierarchyDescriptorRuleSelector(HierarchyDescriptorRulesRepository repository, HierarchyDescriptorRuleDisplayContextMatcher matcher) {
        this.repository = repository;
        this.matcher = matcher;
    }

    public Optional<HierarchyDescriptorRule> selectRule(ProjectId projectId, DisplayContext displayContext, Set<Capability> actions) {
        var rules = repository.find(projectId);
        return rules.stream()
                .flatMap(rule -> rule.rules().stream())
                .filter(rule -> matcher.matches(rule, displayContext, actions))
                .findFirst();
    }
}
