package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.persistence.Repository;

import java.util.Optional;

public interface HierarchyDescriptorRulesRepository extends Repository {

    void save(ProjectHierarchyDescriptorRules rules);

    void delete(ProjectId projectId);

    Optional<ProjectHierarchyDescriptorRules> find(ProjectId projectId);
}
