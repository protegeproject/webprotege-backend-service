package edu.stanford.protege.webprotege.diff;

import edu.stanford.protege.webprotege.hierarchy.ordering.ProjectOrderedChildren;

import java.util.*;
import java.util.stream.Collectors;

public class ProjectOrderedChildren2DiffElementsTranslator {

    public ProjectOrderedChildren2DiffElementsTranslator() {
    }

    public List<DiffElement<String, OrderChange>> getDiffElementsFromOrdering(
            Optional<ProjectOrderedChildren> oldOrderingOptional,
            ProjectOrderedChildren newOrdering) {

        List<DiffElement<String, OrderChange>> diffs = new ArrayList<>();
        List<String> newOrder = newOrdering.children();

        if (oldOrderingOptional.isEmpty()) {
            for (String child : newOrder) {
                OrderChange orderChange = new OrderChange(null, null, getPreviousEntity(child, newOrder), getNextEntity(child, newOrder));
                diffs.add(new DiffElement<>(DiffOperation.ADD, child, orderChange));
            }
            return diffs;
        }

        List<String> oldOrder = oldOrderingOptional.get().children();
        Set<String> movedChildren = findMovedChildren(oldOrder, newOrder);

        for (String child : movedChildren) {
            OrderChange orderChange = computeOrderChange(child, oldOrder, newOrder);
            diffs.add(new DiffElement<>(DiffOperation.ADD, child, orderChange));
        }

        return diffs;
    }

    private Set<String> findMovedChildren(List<String> oldOrder, List<String> newOrder) {
        Map<String, Integer> newIndexMap = new HashMap<>();
        for (int i = 0; i < newOrder.size(); i++) {
            newIndexMap.put(newOrder.get(i), i);
        }

        return oldOrder.stream()
                .filter(child -> newIndexMap.containsKey(child) &&
                        !Objects.equals(oldOrder.indexOf(child), newIndexMap.get(child))
                )
                .collect(Collectors.toSet());
    }

    private OrderChange computeOrderChange(String child, List<String> oldOrder, List<String> newOrder) {
        return new OrderChange(
                getPreviousEntity(child, oldOrder),
                getNextEntity(child, oldOrder),
                getPreviousEntity(child, newOrder),
                getNextEntity(child, newOrder)
        );
    }

    private String getPreviousEntity(String entity, List<String> order) {
        int index = order.indexOf(entity);
        return (index > 0) ? order.get(index - 1) : null;
    }

    private String getNextEntity(String entity, List<String> order) {
        int index = order.indexOf(entity);
        return (index < order.size() - 1) ? order.get(index + 1) : null;
    }
}
