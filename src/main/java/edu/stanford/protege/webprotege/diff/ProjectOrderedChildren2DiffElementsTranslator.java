package edu.stanford.protege.webprotege.diff;

import edu.stanford.protege.webprotege.hierarchy.ordering.ProjectOrderedChildren;

import java.util.*;

public class ProjectOrderedChildren2DiffElementsTranslator {

    public ProjectOrderedChildren2DiffElementsTranslator() {
    }

    public List<DiffElement<String, OrderChange>> getDiffElementsFromOrdering(
            Optional<ProjectOrderedChildren> oldOrderingOptional,
            ProjectOrderedChildren newOrdering) {

        List<DiffElement<String, OrderChange>> diffs = new ArrayList<>();
        List<String> newOrder = newOrdering.children();

        // Case 1: If there is no previous ordering, treat every element as new.
        if (oldOrderingOptional.isEmpty()) {
            for (String child : newOrder) {
                OrderChange orderChange = new OrderChange(
                        null,
                        null,
                        getPreviousEntity(child, newOrder),
                        getNextEntity(child, newOrder)
                );
                diffs.add(new DiffElement<>(DiffOperation.ADD, child, orderChange));
            }
            return diffs;
        }

        // Case 2: Compare old vs new order using LIS
        List<String> oldOrder = oldOrderingOptional.get().children();

        // Step A: Build a map from child to its index in the new order
        Map<String, Integer> newIndexMap = new HashMap<>();
        for (int i = 0; i < newOrder.size(); i++) {
            newIndexMap.put(newOrder.get(i), i);
        }

        // Step B: Transform the old order into a list of indices (in the new order)
        List<Integer> transformed = new ArrayList<>();
        for (String child : oldOrder) {
            Integer idx = newIndexMap.get(child);
            // Only add if child actually exists in new order
            if (idx != null) {
                transformed.add(idx);
            }
        }

        // Step C: Compute the LIS *once*
        Set<Integer> lisIndexSet = longestIncreasingSubsequence(transformed);

        // Step D: Identify which old-order children are not in that LIS
        List<String> movedChildren = new ArrayList<>();
        for (String child : oldOrder) {
            Integer idx = newIndexMap.get(child);
            if (idx != null && !lisIndexSet.contains(idx)) {
                movedChildren.add(child);
            }
        }

        // Step E: For each moved child, create a DiffElement capturing old vs new neighbors
        for (String child : movedChildren) {
            // Old neighbors
            String oldPrev = getPreviousEntity(child, oldOrder);
            String oldNext = getNextEntity(child, oldOrder);

            // New neighbors (using the final new order)
            String newPrev = getPreviousEntity(child, newOrder);
            String newNext = getNextEntity(child, newOrder);

            OrderChange orderChange = new OrderChange(oldPrev, oldNext, newPrev, newNext);
            diffs.add(new DiffElement<>(DiffOperation.ADD, child, orderChange));
        }

        return diffs;
    }

    /**
     * Computes the Longest Increasing Subsequence (LIS) of a list of integers,
     * returning a set of the *values* in the LIS.
     * For example, if inputList = [2, 5, 3, 10] and one valid LIS is [2, 3, 10],
     * this method returns {2, 3, 10}.
     */
    private Set<Integer> longestIncreasingSubsequence(List<Integer> inputList) {
        // tailValues holds the smallest ending value for an increasing subsequence of a given length.
        // For example, tailValues.get(0) is the smallest ending value of a subsequence of length 1,
        // tailValues.get(1) is the smallest ending value of a subsequence of length 2, and so on.
        List<Integer> tailValues = new ArrayList<>();

        // tailPositionMap maps an index 'i' in inputList to the position in tailValues
        // that represents the length (minus one) of the longest increasing subsequence ending at inputList[i].
        Map<Integer, Integer> tailPositionMap = new HashMap<>();

        // predecessorMap maps each index in inputList to the index of its predecessor in the subsequence.
        // This will allow us to reconstruct the actual subsequence later.
        Map<Integer, Integer> predecessorMap = new HashMap<>();

        // Process each element in inputList
        for (int i = 0; i < inputList.size(); i++) {
            int currentValue = inputList.get(i);

            // Determine where currentValue should be inserted into tailValues using binary search.
            int pos = Collections.binarySearch(tailValues, currentValue);
            if (pos < 0) {
                // If not found, binarySearch returns -(insertion_point + 1).
                pos = -(pos + 1);
            }

            // If currentValue is larger than all values in tailValues, it extends the subsequence.
            if (pos == tailValues.size()) {
                tailValues.add(currentValue);
            } else {
                // Otherwise, replace the value at the found position to keep the smallest possible tail value.
                tailValues.set(pos, currentValue);
            }

            // Record that the element at index i fits into a subsequence of length pos+1.
            tailPositionMap.put(i, pos);

            // If the element is not the first in the subsequence, set its predecessor.
            if (pos > 0) {
                // Find an index j < i such that inputList[j] is the last element
                // of an increasing subsequence of length pos.
                for (int j = i - 1; j >= 0; j--) {
                    if (tailPositionMap.get(j) == pos - 1) {
                        predecessorMap.put(i, j);
                        break;
                    }
                }
            }
        }

        // Now, reconstruct the longest increasing subsequence (LIS) using the predecessorMap.
        Set<Integer> lisValues = new HashSet<>();
        int targetTailIndex = tailValues.size() - 1;
        Integer lastIndexInLIS = null;

        // Find an index in inputList that ends an increasing subsequence of maximum length.
        for (int i = 0; i < inputList.size(); i++) {
            if (tailPositionMap.get(i) == targetTailIndex) {
                lastIndexInLIS = i;
                break;
            }
        }

        // Trace backwards using predecessorMap to collect all the values in the LIS.
        while (lastIndexInLIS != null) {
            lisValues.add(inputList.get(lastIndexInLIS));
            lastIndexInLIS = predecessorMap.get(lastIndexInLIS);
        }

        return lisValues;
    }

    private String getPreviousEntity(String entity, List<String> order) {
        int index = order.indexOf(entity);
        return (index > 0) ? order.get(index - 1) : null;
    }

    private String getNextEntity(String entity, List<String> order) {
        int index = order.indexOf(entity);
        return (index >= 0 && index < order.size() - 1) ? order.get(index + 1) : null;
    }
}
