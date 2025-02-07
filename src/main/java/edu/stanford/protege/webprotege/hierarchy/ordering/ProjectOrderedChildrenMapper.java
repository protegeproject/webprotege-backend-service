package edu.stanford.protege.webprotege.hierarchy.ordering;

import edu.stanford.protege.webprotege.common.*;
import edu.stanford.protege.webprotege.hierarchy.ordering.dtos.OrderedChildren;

import java.util.*;
import java.util.stream.Collectors;

public class ProjectOrderedChildrenMapper {

    public static Set<ProjectOrderedChildren> mapToProjectOrderedChildren(OrderedChildren orderedChildren, ProjectId projectId, UserId userId) {
        return orderedChildren.orderedChildren().stream()
                .map(child -> new ProjectOrderedChildren(
                        child.orderedChild(),
                        projectId,
                        orderedChildren.entityUri(),
                        userId.id(),
                        child.orderedChildIndex()
                ))
                .collect(Collectors.toSet());
    }
}
