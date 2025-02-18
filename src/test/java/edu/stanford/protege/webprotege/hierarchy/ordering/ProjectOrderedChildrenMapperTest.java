package edu.stanford.protege.webprotege.hierarchy.ordering;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.hierarchy.ordering.dtos.OrderedChild;
import edu.stanford.protege.webprotege.hierarchy.ordering.dtos.OrderedChildren;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProjectOrderedChildrenMapperTest {

    @Test
    void GIVEN_orderedChildrenWithMultipleEntries_WHEN_mapToProjectOrderedChildren_THEN_shouldMapCorrectlyAndSortByIndex() {
        OrderedChild child1 = new OrderedChild("child-1", "20000000");
        OrderedChild child2 = new OrderedChild("child-2", "10000000"); // This should be sorted before child1
        OrderedChildren orderedChildren = new OrderedChildren(List.of(child1, child2), "parent-entity-uri");
        ProjectId projectId = new ProjectId("test-project-id");
        UserId userId = new UserId("test-user-id");

        ProjectOrderedChildren result = ProjectOrderedChildrenMapper.mapToProjectOrderedChildren(orderedChildren, projectId, userId);

        assertEquals("parent-entity-uri", result.entityUri());
        assertEquals("test-project-id", result.projectId().id());
        assertEquals("test-user-id", result.userId());
        assertEquals(2, result.children().size());

        assertEquals("child-2", result.children().get(0)); // Sorted first by index
        assertEquals("child-1", result.children().get(1));
    }

    @Test
    void GIVEN_orderedChildrenWithNullUserId_WHEN_mapToProjectOrderedChildren_THEN_shouldSetUserIdToNull() {
        OrderedChild child = new OrderedChild("child-1", "10000000");
        OrderedChildren orderedChildren = new OrderedChildren(List.of(child), "parent-entity-uri");
        ProjectId projectId = new ProjectId("test-project-id");

        ProjectOrderedChildren result = ProjectOrderedChildrenMapper.mapToProjectOrderedChildren(orderedChildren, projectId, null);

        assertEquals("parent-entity-uri", result.entityUri());
        assertEquals("test-project-id", result.projectId().id());
        assertNull(result.userId());
        assertEquals(1, result.children().size());
        assertEquals("child-1", result.children().get(0));
    }

    @Test
    void GIVEN_emptyOrderedChildrenList_WHEN_mapToProjectOrderedChildren_THEN_shouldReturnEmptyChildrenList() {
        OrderedChildren orderedChildren = new OrderedChildren(List.of(), "parent-entity-uri");
        ProjectId projectId = new ProjectId("test-project-id");
        UserId userId = new UserId("test-user-id");

        ProjectOrderedChildren result = ProjectOrderedChildrenMapper.mapToProjectOrderedChildren(orderedChildren, projectId, userId);

        assertEquals("parent-entity-uri", result.entityUri());
        assertEquals("test-project-id", result.projectId().id());
        assertEquals("test-user-id", result.userId());
        assertTrue(result.children().isEmpty());
    }
}
