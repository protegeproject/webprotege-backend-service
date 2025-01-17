package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.common.ProjectId;

public interface HierarchyDescriptorRuleManager {

    void addAll(Iterable<HierarchyDescriptorRule> rule);

    void removeAll(Iterable<HierarchyDescriptorRule> rule);

    void clear(ProjectId projectId);
}
