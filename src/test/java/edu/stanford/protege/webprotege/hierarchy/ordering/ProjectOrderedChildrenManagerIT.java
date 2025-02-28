package edu.stanford.protege.webprotege.hierarchy.ordering;

import edu.stanford.protege.webprotege.MongoTestExtension;
import edu.stanford.protege.webprotege.RabbitTestExtension;
import edu.stanford.protege.webprotege.WebprotegeBackendMonolithApplication;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.locking.ReadWriteLockService;
import edu.stanford.protege.webprotege.revision.uiHistoryConcern.NewRevisionsEventEmitterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.semanticweb.owlapi.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

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

    @MockitoSpyBean
    private NewRevisionsEventEmitterService newRevisionsEventEmitterService;

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

        Query queryA = new Query(Criteria.where(ProjectOrderedChildren.ENTITY_URI).is(parentA.toStringID()));
        ProjectOrderedChildren parentAEntry = mongoTemplate.findOne(queryA, ProjectOrderedChildren.class, ProjectOrderedChildren.ORDERED_CHILDREN_COLLECTION);
        assertNull(parentAEntry);

        Query queryB = new Query(Criteria.where(ProjectOrderedChildren.ENTITY_URI).is(parentB.toStringID()));
        ProjectOrderedChildren parentBEntry = mongoTemplate.findOne(queryB, ProjectOrderedChildren.class, ProjectOrderedChildren.ORDERED_CHILDREN_COLLECTION);
        assertNotNull(parentBEntry);
        assertTrue(parentBEntry.children().containsAll(List.of(child1.toStringID(), child2.toStringID())), "Both children should be moved to parentB");
    }


}
