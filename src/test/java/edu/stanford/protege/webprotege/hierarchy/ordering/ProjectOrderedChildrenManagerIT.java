package edu.stanford.protege.webprotege.hierarchy.ordering;

import edu.stanford.protege.webprotege.MongoTestExtension;
import edu.stanford.protege.webprotege.RabbitTestExtension;
import edu.stanford.protege.webprotege.WebprotegeBackendMonolithApplication;
import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.hierarchy.ordering.dtos.UpdateEntityChildrenRequest;
import edu.stanford.protege.webprotege.hierarchy.ordering.dtos.UpdateEntityChildrenResponse;
import edu.stanford.protege.webprotege.ipc.CommandExecutor;
import edu.stanford.protege.webprotege.locking.ReadWriteLockService;
import edu.stanford.protege.webprotege.revision.uiHistoryConcern.NewRevisionsEventEmitterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@Import({WebprotegeBackendMonolithApplication.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@ExtendWith({SpringExtension.class, MongoTestExtension.class, RabbitTestExtension.class})
@ActiveProfiles("test")
public class ProjectOrderedChildrenManagerIT {

    @Autowired
    private ProjectOrderedChildrenServiceImpl projectOrderedChildrenService;

    @Autowired
    private ReadWriteLockService readWriteLockService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @MockitoBean
    private NewRevisionsEventEmitterService newRevisionsEventEmitterService;

    @MockitoBean
    private CommandExecutor<UpdateEntityChildrenRequest, UpdateEntityChildrenResponse> executor;

    private ProjectOrderedChildrenManager manager;
    private ProjectId projectId;

    private final OWLClass parentA = new OWLClassImpl(IRI.create("http://example.com/entity/parentA"));
    private final OWLClass parentB = new OWLClassImpl(IRI.create("http://example.com/entity/parentB"));
    private final OWLClass child1 = new OWLClassImpl(IRI.create("http://example.com/entity/child1"));
    private final OWLClass child2 = new OWLClassImpl(IRI.create("http://example.com/entity/child2"));

    @BeforeEach
    void setUp() {
        mongoTemplate.dropCollection(ProjectOrderedChildren.class);
        projectId = new ProjectId(UUID.randomUUID().toString());
        when(executor.execute(any(), any())).thenReturn(CompletableFuture.supplyAsync(UpdateEntityChildrenResponse::new));
        manager = new ProjectOrderedChildrenManager(projectId, projectOrderedChildrenService, readWriteLockService, newRevisionsEventEmitterService);

        projectOrderedChildrenService.addChildToParent(projectId, parentA.toStringID(), child1.toStringID());
        projectOrderedChildrenService.addChildToParent(projectId, parentA.toStringID(), child2.toStringID());
    }

    @Test
    public void GIVEN_existingHierarchy_WHEN_moveHierarchyNode_THEN_childIsReassignedToNewParent() {
        manager.moveHierarchyNode(parentA, parentB, child1);

        Query queryA = new Query(Criteria.where(ProjectOrderedChildren.ENTITY_URI).is(parentA.toStringID()));
        ProjectOrderedChildren parentAEntry = mongoTemplate.findOne(queryA, ProjectOrderedChildren.class, ProjectOrderedChildren.ORDERED_CHILDREN_COLLECTION);
        assertNotNull(parentAEntry);
        assertFalse(parentAEntry.children().contains(child1.toStringID()), "Child1 should be removed from parentA");

        Query queryB = new Query(Criteria.where(ProjectOrderedChildren.ENTITY_URI).is(parentB.toStringID()));
        ProjectOrderedChildren parentBEntry = mongoTemplate.findOne(queryB, ProjectOrderedChildren.class, ProjectOrderedChildren.ORDERED_CHILDREN_COLLECTION);
        assertNotNull(parentBEntry);
        assertTrue(parentBEntry.children().contains(child1.toStringID()), "Child1 should be assigned to parentB");
    }

    @Test
    public void GIVEN_multipleEntities_WHEN_moveEntitiesToParent_THEN_entitiesAreReassignedCorrectly() {
        OWLClass newParent = parentB;
        Set<OWLClass> entitiesToMove = Set.of(child1, child2);

        Map<String, List<String>> previousParents = Map.of(
                child1.toStringID(), List.of(parentA.toStringID()),
                child2.toStringID(), List.of(parentA.toStringID())
        );

        manager.moveEntitiesToParent(newParent, entitiesToMove, previousParents);

        Query queryB = new Query(Criteria.where(ProjectOrderedChildren.ENTITY_URI).is(parentB.toStringID()));
        ProjectOrderedChildren parentBEntry = mongoTemplate.findOne(queryB, ProjectOrderedChildren.class, ProjectOrderedChildren.ORDERED_CHILDREN_COLLECTION);
        assertNotNull(parentBEntry);
        assertTrue(parentBEntry.children().containsAll(List.of(child1.toStringID(), child2.toStringID())), "Both children should be moved to parentB");

        Query queryA = new Query(Criteria.where(ProjectOrderedChildren.ENTITY_URI).is(parentA.toStringID()));
        ProjectOrderedChildren parentAEntry = mongoTemplate.findOne(queryA, ProjectOrderedChildren.class, ProjectOrderedChildren.ORDERED_CHILDREN_COLLECTION);
        assertNull(parentAEntry);
    }

    @Test
    public void GIVEN_existingChildrenOrdering_WHEN_updateChildrenOrderingForEntity_THEN_childrenOrderingIsUpdated() {
        List<String> newOrder = List.of(child2.toStringID(), child1.toStringID());
        UserId userId = new UserId("testUser");
        ChangeRequestId changeRequestId = new ChangeRequestId("testChangeRequest");
        String commitMessage = "Update children ordering for parentA";

        manager.updateChildrenOrderingForEntity(IRI.create(parentA.toStringID()),
                newOrder, userId, changeRequestId, commitMessage);

        Query query = new Query(Criteria.where(ProjectOrderedChildren.ENTITY_URI).is(parentA.toStringID()));
        ProjectOrderedChildren updatedOrdering = mongoTemplate.findOne(query, ProjectOrderedChildren.class, ProjectOrderedChildren.ORDERED_CHILDREN_COLLECTION);
        assertNotNull(updatedOrdering, "Updated ordering should exist for parentA");
        assertEquals(newOrder, updatedOrdering.children(), "The children ordering should be updated");

        verify(newRevisionsEventEmitterService, times(1)).emitNewProjectOrderedChildrenEvent(
                eq(IRI.create(parentA.toStringID())),
                any(Optional.class),
                any(Optional.class),
                eq(userId),
                eq(changeRequestId),
                eq(commitMessage)
        );
    }

    @Test
    public void GIVEN_noExistingChildrenOrdering_WHEN_updateChildrenOrderingForEntity_THEN_newOrderingIsCreated() {
        OWLClass parentC = new OWLClassImpl(IRI.create("http://example.com/entity/parentC"));
        List<String> newOrder = List.of(child1.toStringID());
        UserId userId = new UserId("testUser2");
        ChangeRequestId changeRequestId = new ChangeRequestId("testChangeRequest2");
        String commitMessage = "Create new ordering for parentC";

        manager.updateChildrenOrderingForEntity(IRI.create(parentC.toStringID()),
                newOrder, userId, changeRequestId, commitMessage);

        Query query = new Query(Criteria.where(ProjectOrderedChildren.ENTITY_URI).is(parentC.toStringID()));
        ProjectOrderedChildren newOrdering = mongoTemplate.findOne(query, ProjectOrderedChildren.class, ProjectOrderedChildren.ORDERED_CHILDREN_COLLECTION);
        assertNotNull(newOrdering, "New ordering should be created for parentC");
        assertEquals(newOrder, newOrdering.children(), "The children ordering should be set as specified");

        ArgumentCaptor<Optional> initialOrderingCaptor = ArgumentCaptor.forClass(Optional.class);
        verify(newRevisionsEventEmitterService, times(1)).emitNewProjectOrderedChildrenEvent(
                eq(IRI.create(parentC.toStringID())),
                initialOrderingCaptor.capture(),
                any(Optional.class),
                eq(userId),
                eq(changeRequestId),
                eq(commitMessage)
        );
        assertFalse(initialOrderingCaptor.getValue().isPresent(), "Initial ordering should be empty for a new parent");
    }

    @Test
    public void GIVEN_duplicateChildrenInNewOrder_WHEN_updateChildrenOrderingForEntity_THEN_throwsIllegalArgumentException() {
        List<String> newOrder = List.of(child1.toStringID(), child1.toStringID());
        UserId userId = new UserId("testUser3");
        ChangeRequestId changeRequestId = new ChangeRequestId("testChangeRequest3");
        String commitMessage = "Attempt to update with duplicate children ordering";

        RuntimeException e = assertThrows(RuntimeException.class,
                () -> manager.updateChildrenOrderingForEntity(
                        IRI.create(parentA.toStringID()),
                        newOrder,
                        userId,
                        changeRequestId,
                        commitMessage
                )
        );

        assertInstanceOf(IllegalArgumentException.class, e.getCause(), "Exception should be caused by IllegalArgumentException");
    }

}
