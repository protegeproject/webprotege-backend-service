package edu.stanford.protege.webprotege.hierarchy.ordering;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.hierarchy.ordering.dtos.*;

import java.util.*;
import java.util.stream.Collectors;

public class ProjectOrderedChildrenMapper {

    public static EntityChildrenOrdering mapToProjectOrderedChildren(OrderedChildren orderedChildren, ProjectId projectId, UserId userId) {
        List<String> sortedChildren = orderedChildren.orderedChildren().stream()
                .sorted(Comparator.comparingInt(c -> Integer.parseInt(c.orderedChildIndex())))
                .map(OrderedChild::orderedChild)
                .collect(Collectors.toList());

        return new EntityChildrenOrdering(
                orderedChildren.entityUri(),
                projectId,
                sortedChildren,
                userId != null ? userId.id() : null
        );
    }
}
