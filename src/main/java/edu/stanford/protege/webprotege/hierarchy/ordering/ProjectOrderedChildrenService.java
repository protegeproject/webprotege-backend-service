package edu.stanford.protege.webprotege.hierarchy.ordering;

import edu.stanford.protege.webprotege.common.*;
import edu.stanford.protege.webprotege.hierarchy.ordering.dtos.OrderedChildren;

import java.util.*;
import java.util.function.Consumer;

public interface ProjectOrderedChildrenService {

    Consumer<List<OrderedChildren>> createBatchProcessorForSavingPaginatedOrderedChildren(ProjectId projectId);

    void saveMultipleProjectOrderedChildren(Set<ProjectOrderedChildren> siblingsOrderingsToBeSaved);

    Set<ProjectOrderedChildren> createProjectOrderedChildren(OrderedChildren orderedChildren, ProjectId projectId, UserId userId);
}
