package edu.stanford.protege.webprotege.hierarchy.ordering;

import edu.stanford.protege.webprotege.common.*;
import edu.stanford.protege.webprotege.dispatch.handlers.SaveEntityChildrenOrderingAction;
import edu.stanford.protege.webprotege.hierarchy.ordering.dtos.OrderedChildren;
import org.semanticweb.owlapi.model.IRI;

import java.util.*;
import java.util.function.Consumer;

public interface ProjectOrderedChildrenService {

    Consumer<List<OrderedChildren>> createBatchProcessorForImportingPaginatedOrderedChildren(ProjectId projectId, boolean overrideExisting);

    void importMultipleProjectOrderedChildren(Set<ProjectOrderedChildren> siblingsOrderingsToBeSaved, boolean overrideExisting);

    ProjectOrderedChildren createProjectOrderedChildren(OrderedChildren orderedChildren, ProjectId projectId, UserId userId);

    void addChildToParent(ProjectId projectId, String parentUri, String newChildUri);

    void removeChildFromParent(ProjectId projectId, String parentUri, String childUriToRemove);

    void updateEntity(SaveEntityChildrenOrderingAction action, UserId userId);

    Optional<ProjectOrderedChildren> findOrderedChildren(ProjectId projectId, IRI parentEntityIri, UserId userId);

    Optional<ProjectOrderedChildren> findOrderedChildren(ProjectId projectId, IRI parentEntityIri);

    Optional<ProjectOrderedChildren> updateEntityAndGet(IRI parentEntityIri, ProjectId projectId, List<String> newChildrenOrder, UserId userId);

    Optional<ProjectOrderedChildren> updateEntityAndGet(IRI parentEntityIri, ProjectId projectId, List<String> newChildrenOrder, Optional<ProjectOrderedChildren> initialOrder, UserId userId);
}
