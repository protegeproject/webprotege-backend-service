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

        /* Step A: Build a map from child to its index in the new order
        We assume that each child is unique in the list, this is important and is a precondition
         */
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

        /* Step C: Compute the LIS *once*
        basically the set will contain the indexes that are still
        in the initial order and were not changed in the new order
        */
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
     * Computes the Longest Increasing Subsequence (LIS) from a list of integers.
     * Returns a set of the values that form one valid longest increasing subsequence.
     *
     * For example, if inputList = [2, 5, 3, 10] and one valid LIS is [2, 3, 10],
     * this method returns {2, 3, 10}.
     */
    private Set<Integer> longestIncreasingSubsequence(List<Integer> inputList) {
        /*
        tailElements holds the smallest possible ending value for an increasing subsequence
         of a given length. For example, tailElements.get(0) is the smallest ending value
        for any subsequence of length 1, tailElements.get(1) for length 2, and so on.
         */
        List<Integer> tailElements = new ArrayList<>();

        /*
        indexToTailPos maps each index 'i' in inputList to the "tail position" (or subsequence length minus one)
         that the element at inputList[i] contributes to.
         */
        Map<Integer, Integer> indexToTailPos = new HashMap<>();

        /* previousIndex helps us backtrack the actual subsequence.
         For each index in inputList, it stores the index of the previous element in the subsequence.
         */
        Map<Integer, Integer> previousIndex = new HashMap<>();

        // Process each element in the inputList.
        for (int currentIndex = 0; currentIndex < inputList.size(); currentIndex++) {
            int currentValue = inputList.get(currentIndex);

            /* Determine where currentValue fits into tailElements using binary search.
             If currentValue is not found, binarySearch returns -(insertionPoint + 1).
             */
            int insertionPos = Collections.binarySearch(tailElements, currentValue);
            if (insertionPos < 0) {
                insertionPos = -(insertionPos + 1);
            }

            /* If currentValue is greater than all elements in tailElements, append it.
             Otherwise, replace the element at the found insertion position.
             */
            if (insertionPos == tailElements.size()) {
                tailElements.add(currentValue);
            } else {
                tailElements.set(insertionPos, currentValue);
            }

            // Record the "tail position" for currentIndex.
            indexToTailPos.put(currentIndex, insertionPos);

            /* If currentValue is not the first element in a subsequence,
             determine its predecessor: find the most recent index with tail position (insertionPos - 1).
             */
            if (insertionPos > 0) {
                for (int searchIndex = currentIndex - 1; searchIndex >= 0; searchIndex--) {
                    if (indexToTailPos.get(searchIndex) == insertionPos - 1) {
                        previousIndex.put(currentIndex, searchIndex);
                        break;
                    }
                }
            }
        }

        // The size of tailElements indicates the length of the longest increasing subsequence.
        int lisLength = tailElements.size();

        // Find an index in inputList that ends an increasing subsequence of maximum length.
        int targetTailPos = lisLength - 1;
        Integer lastIndexInLIS = null;
        for (int i = 0; i < inputList.size(); i++) {
            if (indexToTailPos.get(i) == targetTailPos) {
                lastIndexInLIS = i;
                break;
            }
        }

        /* Backtrack through previousIndex to reconstruct one valid longest increasing subsequence.
         (Since we're returning a Set, the order won't be preserved.)
         */
        Set<Integer> lisValues = new HashSet<>();
        while (lastIndexInLIS != null) {
            lisValues.add(inputList.get(lastIndexInLIS));
            lastIndexInLIS = previousIndex.get(lastIndexInLIS);
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
