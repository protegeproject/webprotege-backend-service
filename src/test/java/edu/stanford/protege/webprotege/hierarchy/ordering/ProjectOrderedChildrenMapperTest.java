package edu.stanford.protege.webprotege.hierarchy.ordering;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.hierarchy.ordering.dtos.OrderedChild;
import edu.stanford.protege.webprotege.hierarchy.ordering.dtos.OrderedChildren;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProjectOrderedChildrenMapperTest {

    @Test
    void GIVEN_orderedChildrenWithMultipleEntries_WHEN_mapToProjectOrderedChildren_THEN_shouldMapCorrectly() {
        OrderedChild child1 = new OrderedChild("child-1", "10000000");
        OrderedChild child2 = new OrderedChild("child-2", "20000000");
        OrderedChildren orderedChildren = new OrderedChildren(List.of(child1, child2), "parent-entity-uri");
        ProjectId projectId = new ProjectId("test-project-id");
        UserId userId = new UserId("test-user-id");

        Set<ProjectOrderedChildren> result = ProjectOrderedChildrenMapper.mapToProjectOrderedChildren(orderedChildren, projectId, userId);

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(poc -> poc.entityUri().equals("child-1") && poc.index().equals("10000000")));
        assertTrue(result.stream().anyMatch(poc -> poc.entityUri().equals("child-2") && poc.index().equals("20000000")));
    }

    @Test
    void GIVEN_orderedChildrenWithNullUserId_WHEN_mapToProjectOrderedChildren_THEN_shouldSetUserIdToNull() {
        OrderedChild child = new OrderedChild("child-1", "10000000");
        OrderedChildren orderedChildren = new OrderedChildren(List.of(child), "parent-entity-uri");
        ProjectId projectId = new ProjectId("test-project-id");

        Set<ProjectOrderedChildren> result = ProjectOrderedChildrenMapper.mapToProjectOrderedChildren(orderedChildren, projectId, null);

        assertEquals(1, result.size());
        assertNull(result.iterator().next().userId());
    }

    @Test
    void GIVEN_emptyOrderedChildrenList_WHEN_mapToProjectOrderedChildren_THEN_shouldReturnEmptySet() {
        OrderedChildren orderedChildren = new OrderedChildren(List.of(), "parent-entity-uri");
        ProjectId projectId = new ProjectId("test-project-id");
        UserId userId = new UserId("test-user-id");

        Set<ProjectOrderedChildren> result = ProjectOrderedChildrenMapper.mapToProjectOrderedChildren(orderedChildren, projectId, userId);

        assertTrue(result.isEmpty());
    }
}
