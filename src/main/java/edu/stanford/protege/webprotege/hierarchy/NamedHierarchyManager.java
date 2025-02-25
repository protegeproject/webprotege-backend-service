package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.common.ProjectId;

import java.util.List;

public interface NamedHierarchyManager {

    List<NamedHierarchy> getNamedHierarchies(ProjectId projectId);

    void setNamedHierarchies(ProjectId projectId, List<NamedHierarchy> namedHierarchies);
}
