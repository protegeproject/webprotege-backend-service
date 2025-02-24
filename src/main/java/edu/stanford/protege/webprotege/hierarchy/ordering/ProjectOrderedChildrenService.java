package edu.stanford.protege.webprotege.hierarchy.ordering;

import edu.stanford.protege.webprotege.common.*;
import edu.stanford.protege.webprotege.dispatch.actions.SaveEntityChildrenOrderingAction;
import edu.stanford.protege.webprotege.hierarchy.ordering.dtos.OrderedChildren;

import java.util.*;
import java.util.function.Consumer;

public interface ProjectOrderedChildrenService {

    Consumer<List<OrderedChildren>> createBatchProcessorForImportingPaginatedOrderedChildren(ProjectId projectId, boolean overrideExisting);

    void importMultipleProjectOrderedChildren(Set<ProjectOrderedChildren> siblingsOrderingsToBeSaved, boolean overrideExisting);

    ProjectOrderedChildren createProjectOrderedChildren(OrderedChildren orderedChildren, ProjectId projectId, UserId userId);

    void addChildToParent(ProjectId projectId, String parentUri, String newChildUri);

    void updateEntity(SaveEntityChildrenOrderingAction action, UserId userId);
}
