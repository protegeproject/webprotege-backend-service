package edu.stanford.protege.webprotege.hierarchy.ordering;

import edu.stanford.protege.webprotege.common.*;
import edu.stanford.protege.webprotege.hierarchy.ordering.dtos.OrderedChild;
import edu.stanford.protege.webprotege.hierarchy.ordering.dtos.OrderedChildren;

import java.util.*;
import java.util.stream.Collectors;

public class ProjectOrderedChildrenMapper {

    public static Set<EntityChildrenOrdering> mapToProjectOrderedChildren(List<OrderedChildren> orderedChildren,
                                                                          ProjectId projectId,
                                                                          UserId userId) {
        return orderedChildren.stream()
                .map(child -> new EntityChildrenOrdering(
                        child.entityUri(),
                        projectId,
                        child.orderedChildren().stream().sorted(Comparator.comparing(OrderedChild::orderedChildIndex)).map(OrderedChild::orderedChild).toList(),
                        userId!=null?userId.id():null
                ))
                .collect(Collectors.toSet());
    }
}
