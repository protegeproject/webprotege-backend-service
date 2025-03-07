package edu.stanford.protege.webprotege.diff;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.hierarchy.ordering.ProjectOrderedChildren;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProjectOrderedChildren2DiffElementsTranslatorTest {

    private ProjectOrderedChildren2DiffElementsTranslator translator;

    @BeforeEach
    void setUp() {
        translator = new ProjectOrderedChildren2DiffElementsTranslator();
    }


    @Test
    void GIVEN_noOldOrdering_WHEN_newOrderingProvided_THEN_allElementsMarkedAsAdded() {
        List<String> newOrder = List.of("A", "B", "C");
        ProjectOrderedChildren newOrdering = new ProjectOrderedChildren("entity1", new ProjectId("proj1"), newOrder, null);

        List<DiffElement<String, OrderChange>> diffs =
                translator.getDiffElementsFromOrdering(Optional.empty(), newOrdering);

        assertEquals(newOrder.size(), diffs.size(), "All elements should be marked as added");

        for (DiffElement<String, OrderChange> diff : diffs) {
            OrderChange change = diff.getLineElement();
            assertNull(change.initialPreviousElement(), "Initial previous element should be null");
            assertNull(change.initialNextElement(), "Initial next element should be null");
        }

        DiffElement<String, OrderChange> diffA = diffs.stream()
                .filter(d -> d.getSourceDocument().equals("A"))
                .findFirst()
                .orElse(null);
        assertNotNull(diffA, "Diff for element A should exist");
        OrderChange ocA = diffA.getLineElement();
        assertNull(ocA.newPreviousElement(), "For A, new previous element should be null");
        assertEquals("B", ocA.newNextElement(), "For A, new next element should be B");
    }

    @Test
    void GIVEN_sameOldAndNewOrder_WHEN_compareOrderings_THEN_noDiffElementsReturned() {
        List<String> order = List.of("A", "B", "C", "D");
        ProjectOrderedChildren oldOrdering = new ProjectOrderedChildren("entity2", new ProjectId("proj2"), order, null);
        ProjectOrderedChildren newOrdering = new ProjectOrderedChildren("entity2", new ProjectId("proj2"), order, null);

        List<DiffElement<String, OrderChange>> diffs =
                translator.getDiffElementsFromOrdering(Optional.of(oldOrdering), newOrdering);

        assertTrue(diffs.isEmpty(), "No diff elements should be returned when orderings are identical");
    }


    @Test
    void GIVEN_oldOrderDiffersByOneElement_WHEN_orderChanged_THEN_singleElementDiffReturned() {
        List<String> oldOrder = List.of("A", "B", "C", "D");
        List<String> newOrder = List.of("B", "A", "C", "D");
        ProjectOrderedChildren oldOrdering = new ProjectOrderedChildren("entity3", new ProjectId("proj3"), oldOrder, null);
        ProjectOrderedChildren newOrdering = new ProjectOrderedChildren("entity3", new ProjectId("proj3"), newOrder, null);

        List<DiffElement<String, OrderChange>> diffs =
                translator.getDiffElementsFromOrdering(Optional.of(oldOrdering), newOrdering);

        assertEquals(1, diffs.size(), "Exactly one element should be marked as moved");
        DiffElement<String, OrderChange> diffA = diffs.get(0);
        assertEquals("A", diffA.getSourceDocument(), "Element A should be marked as moved");

        OrderChange oc = diffA.getLineElement();
        assertNull(oc.initialPreviousElement(), "Old previous for A should be null");
        assertEquals("B", oc.initialNextElement(), "Old next for A should be B");
        assertEquals("B", oc.newPreviousElement(), "New previous for A should be B");
        assertEquals("C", oc.newNextElement(), "New next for A should be C");
    }


    @Test
    void GIVEN_complexReordering_WHEN_orderChanged_THEN_correctDiffElementsReturned() {
        List<String> oldOrder = List.of("E1", "E2", "E3", "E4", "E5", "E6", "E7", "E8", "E9", "E10", "E11", "E12");
        List<String> newOrder = List.of("E6", "E1", "E2", "E3", "E4", "E5", "E11", "E10", "E9", "E7", "E8", "E12");
        ProjectOrderedChildren oldOrdering = new ProjectOrderedChildren("entity4", new ProjectId("proj4"), oldOrder, null);
        ProjectOrderedChildren newOrdering = new ProjectOrderedChildren("entity4", new ProjectId("proj4"), newOrder, null);

        List<DiffElement<String, OrderChange>> diffs =
                translator.getDiffElementsFromOrdering(Optional.of(oldOrdering), newOrdering);

        assertEquals(4, diffs.size(), "Four elements should be marked as moved");

        Set<String> movedElements = diffs.stream()
                .map(DiffElement::getSourceDocument)
                .collect(Collectors.toSet());
        assertEquals(Set.of("E6", "E9", "E10", "E11"), movedElements, "Moved elements do not match expected set");

        DiffElement<String, OrderChange> diffE6 = diffs.stream()
                .filter(d -> d.getSourceDocument().equals("E6"))
                .findFirst()
                .orElse(null);
        assertNotNull(diffE6, "Diff for element E6 should be present");
        OrderChange ocE6 = diffE6.getLineElement();
        assertEquals("E5", ocE6.initialPreviousElement(), "Old previous for E6 should be E5");
        assertEquals("E7", ocE6.initialNextElement(), "Old next for E6 should be E7");
        assertNull(ocE6.newPreviousElement(), "New previous for E6 should be null");
        assertEquals("E1", ocE6.newNextElement(), "New next for E6 should be E1");
    }
}
