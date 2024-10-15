package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.common.ProjectId;

import java.util.List;

public interface NamedHierarchyManager {

    List<NamedHierarchy> getHierarchies(ProjectId projectId);

    void saveHierarchy(ProjectId projectId, NamedHierarchy namedHierarchy);
}
