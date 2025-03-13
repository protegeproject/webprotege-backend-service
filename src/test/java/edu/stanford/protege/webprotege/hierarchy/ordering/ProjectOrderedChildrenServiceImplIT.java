package edu.stanford.protege.webprotege.hierarchy.ordering;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.protege.webprotege.MongoTestExtension;
import edu.stanford.protege.webprotege.RabbitTestExtension;
import edu.stanford.protege.webprotege.WebprotegeBackendMonolithApplication;
import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.dispatch.handlers.SaveEntityChildrenOrderingAction;
import edu.stanford.protege.webprotege.hierarchy.ordering.dtos.OrderedChild;
import edu.stanford.protege.webprotege.hierarchy.ordering.dtos.OrderedChildren;
import edu.stanford.protege.webprotege.locking.ReadWriteLockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.semanticweb.owlapi.model.IRI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static edu.stanford.protege.webprotege.hierarchy.ordering.ProjectOrderedChildren.ENTITY_URI;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import({WebprotegeBackendMonolithApplication.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@ExtendWith({SpringExtension.class, MongoTestExtension.class, RabbitTestExtension.class})
@ActiveProfiles("test")
public class ProjectOrderedChildrenServiceImplIT {

    @Autowired
    private ProjectOrderedChildrenServiceImpl service;

    @Autowired
    private ProjectOrderedChildrenRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ReadWriteLockService readWriteLockService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private ProjectId projectId;
    private final String parentUri = "http://example.com/entity/parent";
    private final String childUri1 = "http://example.com/entity/child1";
    private final String childUri2 = "http://example.com/entity/child2";
    private final String childUri3 = "http://example.com/entity/child3";

    @BeforeEach
    void setUp() {
        mongoTemplate.dropCollection(ProjectOrderedChildren.class);
        projectId = new ProjectId(UUID.randomUUID().toString());
    }

    @Test
    public void GIVEN_orderedChildren_WHEN_importMultipleProjectOrderedChildren_THEN_persistedCorrectly() {
        OrderedChildren orderedChildren = new OrderedChildren(
                List.of(
                        new OrderedChild(childUri1, "3000000"),
                        new OrderedChild(childUri2, "1000000"),
                        new OrderedChild(childUri3, "2000000")
                ),
                parentUri
        );

        ProjectOrderedChildren projectOrderedChildren = service.createProjectOrderedChildren(orderedChildren, projectId, null);

        service.importMultipleProjectOrderedChildren(Set.of(projectOrderedChildren), false);

        List<ProjectOrderedChildren> storedEntries = mongoTemplate.findAll(ProjectOrderedChildren.class);
        assertFalse(storedEntries.isEmpty(), "ProjectOrderedChildren should be saved in MongoDB");

        ProjectOrderedChildren retrieved = storedEntries.get(0);
        assertEquals(parentUri, retrieved.entityUri(), "Parent URI should match");
        assertEquals(3, retrieved.children().size(), "Should have two children stored");
        assertTrue(retrieved.children().contains(childUri1), "Should contain childUri1");
        assertTrue(retrieved.children().contains(childUri2), "Should contain childUri2");
        assertEquals(retrieved.children().get(0), childUri2);
        assertEquals(retrieved.children().get(1), childUri3);
        assertEquals(retrieved.children().get(2), childUri1);
    }

    @Test
    public void GIVEN_newOrderOnChildren_WHEN_updateEntity_THEN_newOrderIsSaved() {
        ProjectOrderedChildren initialEntry = new ProjectOrderedChildren(parentUri, projectId, List.of(childUri1, childUri2, childUri3), null);
        mongoTemplate.insert(initialEntry, ProjectOrderedChildren.ORDERED_CHILDREN_COLLECTION);

        SaveEntityChildrenOrderingAction action = new SaveEntityChildrenOrderingAction(projectId,
                IRI.create(initialEntry.entityUri()),
                Arrays.asList(childUri3, childUri2, childUri1),
                ChangeRequestId.generate(),
                "This is a commit message");

        service.updateEntity(action, null);

        List<ProjectOrderedChildren> storedEntries = mongoTemplate.findAll(ProjectOrderedChildren.class);
        assertFalse(storedEntries.isEmpty(), "EntityChildrenOrdering should be saved in MongoDB");

        ProjectOrderedChildren retrieved = storedEntries.get(0);
        assertEquals(parentUri, retrieved.entityUri(), "Parent URI should match");
        assertEquals(3, retrieved.children().size(), "Should have two children stored");
        assertTrue(retrieved.children().contains(childUri1), "Should contain childUri1");
        assertTrue(retrieved.children().contains(childUri2), "Should contain childUri2");
        assertEquals(retrieved.children().get(0), childUri3);
        assertEquals(retrieved.children().get(1), childUri2);
        assertEquals(retrieved.children().get(2), childUri1);

    }

    @Test
    public void GIVEN_existingParent_WHEN_overrideExistingTrue_THEN_childrenAreUpdated() {
        ProjectOrderedChildren existingEntry = new ProjectOrderedChildren(parentUri, projectId, List.of(childUri1), null);
        mongoTemplate.insert(existingEntry);

        OrderedChildren orderedChildren = new OrderedChildren(List.of(
                new OrderedChild(childUri2, "1000000"),
                new OrderedChild(childUri3, "2000000")), parentUri);

        ProjectOrderedChildren updatedEntry = service.createProjectOrderedChildren(orderedChildren, projectId, null);

        service.importMultipleProjectOrderedChildren(Set.of(updatedEntry), true);

        Query query = new Query(Criteria.where(ENTITY_URI).is(parentUri));
        Optional<ProjectOrderedChildren> result = Optional.ofNullable(mongoTemplate.findOne(query, ProjectOrderedChildren.class));

        assertTrue(result.isPresent(), "Parent should still exist in DB");
        assertEquals(2, result.get().children().size(), "Children should be replaced");
        assertTrue(result.get().children().contains(childUri2), "Child2 should be present");
        assertTrue(result.get().children().contains(childUri3), "Child3 should be present");
    }

    @Test
    public void GIVEN_existingParent_WHEN_overrideExistingFalse_THEN_childrenAreNotUpdated() {
        ProjectOrderedChildren existingEntry = new ProjectOrderedChildren(parentUri, projectId, List.of(childUri1), null);
        mongoTemplate.insert(existingEntry);

        OrderedChildren orderedChildren = new OrderedChildren(
                List.of(
                        new OrderedChild(childUri2, "1000000"),
                        new OrderedChild(childUri3, "2000000")
                ),
                parentUri
        );

        ProjectOrderedChildren updatedEntry = service.createProjectOrderedChildren(orderedChildren, projectId, null);

        service.importMultipleProjectOrderedChildren(Set.of(updatedEntry), false);

        Query query = new Query(Criteria.where(ENTITY_URI).is(parentUri));
        Optional<ProjectOrderedChildren> result = Optional.ofNullable(mongoTemplate.findOne(query, ProjectOrderedChildren.class));

        assertTrue(result.isPresent(), "Parent should still exist in DB");
        assertEquals(1, result.get().children().size(), "Children should not be replaced");
        assertTrue(result.get().children().contains(childUri1), "Child1 should remain");
    }

    @Test
    public void GIVEN_noExistingParent_WHEN_overrideExistingTrue_THEN_entryIsCreated() {
        OrderedChildren orderedChildren = new OrderedChildren(
                List.of(
                        new OrderedChild(childUri1, "1000000"),
                        new OrderedChild(childUri2, "2000000")
                ),
                parentUri
        );

        ProjectOrderedChildren newEntry = service.createProjectOrderedChildren(orderedChildren, projectId, null);

        service.importMultipleProjectOrderedChildren(Set.of(newEntry), true);

        Query query = new Query(Criteria.where(ENTITY_URI).is(parentUri));
        Optional<ProjectOrderedChildren> result = Optional.ofNullable(mongoTemplate.findOne(query, ProjectOrderedChildren.class));

        assertTrue(result.isPresent(), "New parent should be created");
        assertEquals(2, result.get().children().size(), "All children should be stored");
    }

    @Test
    public void GIVEN_noExistingParent_WHEN_overrideExistingFalse_THEN_entryIsCreated() {
        OrderedChildren orderedChildren = new OrderedChildren(
                List.of(
                        new OrderedChild(childUri1, "1000000"),
                        new OrderedChild(childUri2, "2000000")
                ),
                parentUri
        );

        ProjectOrderedChildren newEntry = service.createProjectOrderedChildren(orderedChildren, projectId, null);

        service.importMultipleProjectOrderedChildren(Set.of(newEntry), false);

        Query query = new Query(Criteria.where(ENTITY_URI).is(parentUri));
        Optional<ProjectOrderedChildren> result = Optional.ofNullable(mongoTemplate.findOne(query, ProjectOrderedChildren.class));

        assertTrue(result.isPresent(), "New parent should be created");
        assertEquals(2, result.get().children().size(), "All children should be stored");
    }

    @Test
    public void GIVEN_multipleExistingParents_WHEN_overrideExistingTrue_THEN_allAreUpdated() {
        ProjectOrderedChildren parent1 = new ProjectOrderedChildren("http://example.com/entity/parent1", projectId, List.of(childUri1), null);
        ProjectOrderedChildren parent2 = new ProjectOrderedChildren("http://example.com/entity/parent2", projectId, List.of(childUri2), null);
        mongoTemplate.insertAll(List.of(parent1, parent2));

        OrderedChildren updatedChildren1 = new OrderedChildren(List.of(new OrderedChild(childUri3, "1000000")), "http://example.com/entity/parent1");
        OrderedChildren updatedChildren2 = new OrderedChildren(List.of(new OrderedChild(childUri1, "2000000")), "http://example.com/entity/parent2");

        ProjectOrderedChildren updatedParent1 = service.createProjectOrderedChildren(updatedChildren1, projectId, null);
        ProjectOrderedChildren updatedParent2 = service.createProjectOrderedChildren(updatedChildren2, projectId, null);

        service.importMultipleProjectOrderedChildren(Set.of(updatedParent1, updatedParent2), true);

        Optional<ProjectOrderedChildren> result1 = Optional.ofNullable(mongoTemplate.findOne(new Query(Criteria.where(ENTITY_URI).is("http://example.com/entity/parent1")), ProjectOrderedChildren.class));
        Optional<ProjectOrderedChildren> result2 = Optional.ofNullable(mongoTemplate.findOne(new Query(Criteria.where(ENTITY_URI).is("http://example.com/entity/parent2")), ProjectOrderedChildren.class));

        assertTrue(result1.isPresent());
        assertEquals(1, result1.get().children().size());
        assertTrue(result1.get().children().contains(childUri3));

        assertTrue(result2.isPresent());
        assertEquals(1, result2.get().children().size());
        assertTrue(result2.get().children().contains(childUri1));
    }

    @Test
    public void GIVEN_multipleExistingParents_WHEN_overrideExistingFalse_THEN_onlyNewEntriesAreInserted() {
        ProjectOrderedChildren parent1 = new ProjectOrderedChildren("http://example.com/entity/parent1", projectId, List.of(childUri1), null);
        mongoTemplate.insert(parent1);

        OrderedChildren updatedChildren1 = new OrderedChildren(List.of(new OrderedChild(childUri3, "1000000")), "http://example.com/entity/parent1");
        OrderedChildren updatedChildren2 = new OrderedChildren(List.of(new OrderedChild(childUri2, "2000000")), "http://example.com/entity/parent2");

        ProjectOrderedChildren updatedParent1 = service.createProjectOrderedChildren(updatedChildren1, projectId, null);
        ProjectOrderedChildren updatedParent2 = service.createProjectOrderedChildren(updatedChildren2, projectId, null);

        service.importMultipleProjectOrderedChildren(Set.of(updatedParent1, updatedParent2), false);

        Optional<ProjectOrderedChildren> result1 = Optional.ofNullable(mongoTemplate.findOne(new Query(Criteria.where(ENTITY_URI).is("http://example.com/entity/parent1")), ProjectOrderedChildren.class));
        Optional<ProjectOrderedChildren> result2 = Optional.ofNullable(mongoTemplate.findOne(new Query(Criteria.where(ENTITY_URI).is("http://example.com/entity/parent2")), ProjectOrderedChildren.class));

        assertTrue(result1.isPresent());
        assertEquals(1, result1.get().children().size());
        assertTrue(result1.get().children().contains(childUri1));

        assertTrue(result2.isPresent());
        assertEquals(1, result2.get().children().size());
        assertTrue(result2.get().children().contains(childUri2));
    }


    @Test
    public void GIVEN_existingParent_WHEN_addChildToParent_THEN_childIsAppended() {
        ProjectOrderedChildren initialEntry = new ProjectOrderedChildren(parentUri, projectId, List.of(childUri1), null);
        mongoTemplate.insert(initialEntry, ProjectOrderedChildren.ORDERED_CHILDREN_COLLECTION);

        service.addChildToParent(projectId, parentUri, childUri2);

        Query query = new Query(Criteria.where("entityUri").is(parentUri));
        Optional<ProjectOrderedChildren> updatedEntry = Optional.ofNullable(mongoTemplate.findOne(query, ProjectOrderedChildren.class, ProjectOrderedChildren.ORDERED_CHILDREN_COLLECTION));

        assertTrue(updatedEntry.isPresent(), "Parent entry should exist after update");
        assertEquals(2, updatedEntry.get().children().size(), "There should be two children now");
        assertTrue(updatedEntry.get().children().contains(childUri2), "New child should be in the list");
    }

    @Test
    public void GIVEN_newParent_WHEN_addChildToParent_THEN_newEntryIsCreated() {
        String newParentUri = "http://example.com/entity/newParent";
        String newChildUri = "http://example.com/entity/newChild";

        service.addChildToParent(projectId, newParentUri, newChildUri);

        Query query = new Query(Criteria.where("entityUri").is(newParentUri));
        Optional<ProjectOrderedChildren> newEntry = Optional.ofNullable(mongoTemplate.findOne(query, ProjectOrderedChildren.class));

        assertTrue(newEntry.isPresent(), "New parent entry should be created");
        assertEquals(1, newEntry.get().children().size(), "There should be one child");
        assertEquals(newChildUri, newEntry.get().children().get(0), "The child should match the inserted one");
    }

    @Test
    public void GIVEN_existingParentWithChildren_WHEN_removeChild_THEN_childIsRemoved() {
        ProjectOrderedChildren initialEntry = new ProjectOrderedChildren(parentUri, projectId, List.of(childUri1, childUri2, childUri3), null);
        mongoTemplate.insert(initialEntry);

        service.removeChildFromParent(projectId, parentUri, childUri2);

        Query query = new Query(Criteria.where(ENTITY_URI).is(parentUri));
        Optional<ProjectOrderedChildren> updatedEntry = Optional.ofNullable(mongoTemplate.findOne(query, ProjectOrderedChildren.class));

        assertTrue(updatedEntry.isPresent(), "Parent entry should still exist");
        assertEquals(2, updatedEntry.get().children().size(), "One child should be removed");
        assertFalse(updatedEntry.get().children().contains(childUri2), "Removed child should no longer be in the list");
        assertTrue(updatedEntry.get().children().contains(childUri1), "Other children should remain");
        assertTrue(updatedEntry.get().children().contains(childUri3), "Other children should remain");
    }

    @Test
    public void GIVEN_existingParentWithOneChild_WHEN_removeChild_THEN_parentEntryIsDeleted() {
        ProjectOrderedChildren initialEntry = new ProjectOrderedChildren(parentUri, projectId, List.of(childUri1), null);
        mongoTemplate.insert(initialEntry);

        service.removeChildFromParent(projectId, parentUri, childUri1);

        Query query = new Query(Criteria.where("entityUri").is(parentUri));
        Optional<ProjectOrderedChildren> updatedEntry = Optional.ofNullable(mongoTemplate.findOne(query, ProjectOrderedChildren.class));

        assertFalse(updatedEntry.isPresent(), "Parent entry should be deleted when last child is removed");
    }

    @Test
    public void GIVEN_nonExistingParent_WHEN_removeChild_THEN_nothingHappens() {
        service.removeChildFromParent(projectId, parentUri, childUri1);

        List<ProjectOrderedChildren> allEntries = mongoTemplate.findAll(ProjectOrderedChildren.class);
        assertTrue(allEntries.isEmpty(), "No changes should be made if parent entry does not exist");
    }

    @Test
    public void GIVEN_existingParent_WHEN_removeNonExistingChild_THEN_noChangesMade() {
        ProjectOrderedChildren initialEntry = new ProjectOrderedChildren(parentUri, projectId, List.of(childUri1, childUri2), null);
        mongoTemplate.insert(initialEntry);

        service.removeChildFromParent(projectId, parentUri, childUri3);

        Query query = new Query(Criteria.where("entityUri").is(parentUri));
        Optional<ProjectOrderedChildren> updatedEntry = Optional.ofNullable(mongoTemplate.findOne(query, ProjectOrderedChildren.class));

        assertTrue(updatedEntry.isPresent(), "Parent entry should still exist");
        assertEquals(2, updatedEntry.get().children().size(), "No child should be removed");
        assertTrue(updatedEntry.get().children().contains(childUri1), "Existing children should remain");
        assertTrue(updatedEntry.get().children().contains(childUri2), "Existing children should remain");
    }

    @Test
    public void GIVEN_existingOrderedChildrenWithNonNullUser_WHEN_findOrderedChildrenWithoutUserId_THEN_returnEntry() {
        // GIVEN an entry with a non-null userId
        String userIdVal = "user123";
        ProjectOrderedChildren entry = new ProjectOrderedChildren(parentUri, projectId, List.of(childUri1, childUri2), userIdVal);
        mongoTemplate.insert(entry, ProjectOrderedChildren.ORDERED_CHILDREN_COLLECTION);

        // WHEN findOrderedChildren is called without providing a userId
        Optional<ProjectOrderedChildren> result = service.findOrderedChildren(projectId, IRI.create(parentUri));

        // THEN the entry is returned with the expected children ordering
        assertTrue(result.isPresent(), "Entry should be returned when an entry exists");
        assertEquals(List.of(childUri1, childUri2), result.get().children(), "Children ordering should match the stored list");
    }

    @Test
    public void GIVEN_existingOrderedChildrenWithNullUser_WHEN_findOrderedChildrenWithoutUserId_THEN_returnEntry() {
        // GIVEN an entry with a null userId
        ProjectOrderedChildren entry = new ProjectOrderedChildren(parentUri, projectId, List.of(childUri1, childUri2), null);
        mongoTemplate.insert(entry, ProjectOrderedChildren.ORDERED_CHILDREN_COLLECTION);

        // WHEN findOrderedChildren is called (it passes null internally as userId)
        Optional<ProjectOrderedChildren> result = service.findOrderedChildren(projectId, IRI.create(parentUri));

        // THEN the entry is returned with the expected children ordering
        assertTrue(result.isPresent(), "Entry should be returned when an entry with null userId exists");
        assertEquals(List.of(childUri1, childUri2), result.get().children(), "Children ordering should match the stored list");
    }

    @Test
    public void GIVEN_noOrderedChildren_WHEN_findOrderedChildrenWithoutUserId_THEN_returnEmptyOptional() {
        // GIVEN no entry exists for a non-existent parent URI
        Optional<ProjectOrderedChildren> result = service.findOrderedChildren(projectId, IRI.create("http://example.com/entity/nonexistent"));

        // THEN an empty Optional is returned
        assertFalse(result.isPresent(), "No entry should be returned if none exists");
    }

    @Test
    public void GIVEN_existingOrderedChildren_WHEN_updateEntityAndGet_THEN_childrenOrderingIsUpdated() {
        // GIVEN an existing entry with an initial ordering and userId "initUser"
        String initialUserId = "initUser";
        ProjectOrderedChildren initialEntry = new ProjectOrderedChildren(parentUri, projectId, List.of(childUri1), initialUserId);
        mongoTemplate.insert(initialEntry, ProjectOrderedChildren.ORDERED_CHILDREN_COLLECTION);

        // WHEN updateEntityAndGet is called to update the ordering with a new list and userId "updateUser"
        List<String> newOrder = List.of(childUri2, childUri3);
        UserId updateUser = new UserId("updateUser");
        Optional<ProjectOrderedChildren> updatedOptional = service.updateEntityAndGet(
                IRI.create(parentUri),
                projectId,
                newOrder,
                Optional.of(initialEntry),
                updateUser
        );

        // THEN the returned entry has the new ordering and is stored in the database
        assertTrue(updatedOptional.isPresent(), "Updated entry should be returned");
        ProjectOrderedChildren updatedEntry = updatedOptional.get();
        assertEquals(newOrder, updatedEntry.children(), "Children ordering should be updated");

        Query query = new Query(Criteria.where(ENTITY_URI).is(parentUri));
        ProjectOrderedChildren storedEntry = mongoTemplate.findOne(query, ProjectOrderedChildren.class, ProjectOrderedChildren.ORDERED_CHILDREN_COLLECTION);
        assertNotNull(storedEntry, "Updated entry should be stored in DB");
        assertEquals(newOrder, storedEntry.children(), "Stored ordering should reflect the updated order");
    }

    @Test
    public void GIVEN_noExistingOrderedChildren_WHEN_updateEntityAndGet_THEN_entryIsInserted() {
        // GIVEN no existing entry for a new parent URI
        String newParentUri = "http://example.com/entity/newParent";
        List<String> newOrder = List.of(childUri1, childUri2);
        UserId updateUser = new UserId("newUser");

        // WHEN updateEntityAndGet is called with an empty Optional
        Optional<ProjectOrderedChildren> insertedOptional = service.updateEntityAndGet(
                IRI.create(newParentUri),
                projectId,
                newOrder,
                Optional.empty(),
                updateUser
        );

        // THEN a new entry is inserted and returned
        assertTrue(insertedOptional.isPresent(), "Inserted entry should be returned");
        ProjectOrderedChildren insertedEntry = insertedOptional.get();
        assertEquals(newParentUri, insertedEntry.entityUri(), "Parent URI should match the new one");
        assertEquals(newOrder, insertedEntry.children(), "Children ordering should match the specified new order");

        Query query = new Query(Criteria.where(ENTITY_URI).is(newParentUri));
        ProjectOrderedChildren storedEntry = mongoTemplate.findOne(query, ProjectOrderedChildren.class, ProjectOrderedChildren.ORDERED_CHILDREN_COLLECTION);
        assertNotNull(storedEntry, "Inserted entry should be stored in DB");
        assertEquals(newOrder, storedEntry.children(), "Stored ordering should reflect the inserted order");
    }

    @Test
    public void GIVEN_duplicateChildrenOrder_WHEN_updateEntityAndGet_THEN_throwsIllegalArgumentException() {
        // GIVEN a duplicate children order list (childUri1 appears twice)
        List<String> duplicateOrder = List.of(childUri1, childUri1);
        UserId updateUser = new UserId("dupUser");

        // WHEN updateEntityAndGet is called, THEN an IllegalArgumentException is thrown
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.updateEntityAndGet(
                        IRI.create(parentUri),
                        projectId,
                        duplicateOrder,
                        Optional.empty(),
                        updateUser
                )
        );
        assertTrue(exception.getMessage().contains("The new children order contains duplicates"),
                "Exception should indicate duplicate entries are not allowed");
    }
}
