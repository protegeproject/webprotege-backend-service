package edu.stanford.protege.webprotege.hierarchy.ordering;

import edu.stanford.protege.webprotege.common.*;
import edu.stanford.protege.webprotege.dispatch.actions.SaveEntityChildrenOrderingAction;
import edu.stanford.protege.webprotege.hierarchy.ordering.dtos.OrderedChildren;
import org.semanticweb.owlapi.model.IRI;

import java.util.*;
import java.util.function.Consumer;

public interface ProjectOrderedChildrenService {

    Consumer<List<OrderedChildren>> createBatchProcessorForImportingPaginatedOrderedChildren(ProjectId projectId);

    void importMultipleProjectOrderedChildren(Set<EntityChildrenOrdering> siblingsOrderingsToBeSaved);

    EntityChildrenOrdering createProjectOrderedChildren(OrderedChildren orderedChildren, ProjectId projectId, UserId userId);

    void addChildToParent(ProjectId projectId, String parentUri, String newChildUri);

    void updateEntity(SaveEntityChildrenOrderingAction action, UserId userId);
}
