package edu.stanford.protege.webprotege.bulkop;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.change.ChangeApplicationResult;
import edu.stanford.protege.webprotege.change.RevisionReverterChangeListGeneratorFactory;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.entity.OWLEntityData;
import edu.stanford.protege.webprotege.hierarchy.ClassHierarchyCycleDetector;
import edu.stanford.protege.webprotege.hierarchy.ClassHierarchyProvider;
import edu.stanford.protege.webprotege.hierarchy.ordering.ProjectOrderedChildrenManager;
import edu.stanford.protege.webprotege.icd.LinearizationParentChecker;
import edu.stanford.protege.webprotege.icd.ReleasedClassesChecker;
import edu.stanford.protege.webprotege.icd.hierarchy.ClassHierarchyRetiredClassDetector;
import edu.stanford.protege.webprotege.ipc.EventDispatcher;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.linearization.LinearizationManager;
import edu.stanford.protege.webprotege.linearization.MergeWithParentEntitiesResponse;
import edu.stanford.protege.webprotege.project.chg.ChangeManager;
import edu.stanford.protege.webprotege.renderer.RenderingManager;
import edu.stanford.protege.webprotege.revision.Revision;
import edu.stanford.protege.webprotege.revision.RevisionManager;
import edu.stanford.protege.webprotege.revision.RevisionNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ChangeEntityParentsActionHandlerTest {

    @Mock
    private AccessManager accessManager;

    @Mock
    private ChangeManager changeManager;

    @Mock
    private ClassHierarchyProvider classHierarchyProvider;

    @Mock
    private RenderingManager renderingManager;

    private RevisionReverterChangeListGeneratorFactory revisionReverterFactory;

    @Mock
    private RevisionManager revisionManager;

    @Mock
    private ReleasedClassesChecker releasedClassesChecker;

    @Mock
    private ClassHierarchyRetiredClassDetector retiredClassDetector;

    @Mock
    private LinearizationManager linearizationManager;

    @Mock
    private LinearizationParentChecker linearizationParentChecker;

    @Mock
    private EditParentsChangeListGeneratorFactory changeListGeneratorFactory;

    @Mock
    private ClassHierarchyCycleDetector classCycleDetector;

    private ProjectId projectId;

    private ChangeEntityParentsActionHandler actionHandler;

    private ExecutionContext executionContext;

    @Mock
    private ProjectOrderedChildrenManager projectOrderedChildrenManager;

    @Mock
    private EventDispatcher eventDispatcher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        projectId = ProjectId.generate();

        RevisionNumber revisionNumber = RevisionNumber.valueOf("1");
        Revision revision = new Revision(UserId.getGuest(), revisionNumber, ImmutableList.of(), 1L, "desc");

        when(revisionManager.getCurrentRevision()).thenReturn(revisionNumber);
        when(revisionManager.getRevision(any())).thenReturn(Optional.of(revision));

        revisionReverterFactory = new RevisionReverterChangeListGeneratorFactory(() -> revisionManager);

        executionContext = new ExecutionContext(UserId.getGuest(), "", UUID.randomUUID().toString());

        actionHandler = new ChangeEntityParentsActionHandler(
                accessManager,
                projectId,
                changeManager,
                changeListGeneratorFactory,
                classCycleDetector,
                revisionReverterFactory,
                revisionManager,
                classHierarchyProvider,
                renderingManager,
                releasedClassesChecker,
                retiredClassDetector,
                linearizationManager,
                linearizationParentChecker,
                projectOrderedChildrenManager, eventDispatcher);
    }

    private OWLClass mockOwlEntityAsClass(OWLEntity entity, String iri) {
        OWLClass owlClass = mock(OWLClass.class);
        IRI entityIri = IRI.create(iri);
        when(entity.getIRI()).thenReturn(entityIri);
        when(entity.asOWLClass()).thenReturn(owlClass);
        when(owlClass.getIRI()).thenReturn(entityIri);
        return owlClass;
    }

    @Test
    void GIVEN_noCyclesOrRetiredParents_WHEN_executeCalled_THEN_resultIsValidAndEmpty() {
        OWLEntity entity = mock(OWLEntity.class);
        OWLClass entityClass = mockOwlEntityAsClass(entity, "http://example.org/entity");

        OWLEntity parent = mock(OWLEntity.class);
        OWLClass parentClass = mockOwlEntityAsClass(parent, "http://example.org/parent");

        ImmutableSet<OWLClass> parents = ImmutableSet.of(parentClass);
        ChangeEntityParentsAction action = new ChangeEntityParentsAction(null, projectId, parents, entityClass, "Test Commit Message");

        when(releasedClassesChecker.isReleased(any())).thenReturn(true);
        when(releasedClassesChecker.getReleasedDescendants(any())).thenReturn(Set.of());
        when(retiredClassDetector.getClassesWithRetiredAncestors(any())).thenReturn(Set.of(parentClass));
        OWLEntityData retiredParentData = mock(OWLEntityData.class);
        when(renderingManager.getRendering(parentClass)).thenReturn(retiredParentData);

        @SuppressWarnings("unchecked")
        ChangeApplicationResult<Boolean> mockChangeResult = mock(ChangeApplicationResult.class);
        when(mockChangeResult.getChangeList()).thenReturn(new ArrayList<>());
        when(mockChangeResult.getSubject()).thenReturn(true);

        when(changeManager.applyChanges(any(), any())).thenAnswer(invocation -> mockChangeResult);

        when(linearizationManager.mergeLinearizationsFromParents(any(), any(), any(), any()))
                .thenReturn(CompletableFuture.completedFuture(new MergeWithParentEntitiesResponse()));

        ChangeEntityParentsResult result = actionHandler.execute(action, executionContext);

        assertNotNull(result);
        // Testul se așteaptă la un rezultat valid și gol, nu la retired parents
        assertTrue(result.classesWithRetiredParents().isEmpty());
        assertTrue(result.classesWithCycle().isEmpty());
        assertTrue(result.oldParentsThatArelinearizationPathParents().isEmpty());

        verify(retiredClassDetector).getClassesWithRetiredAncestors(parents);
    }

    @Test
    @Disabled("Alex Fix this")
    void GIVEN_cyclesDetected_WHEN_executeCalled_THEN_resultContainsCycles() {
        OWLEntity entity = mock(OWLEntity.class);
        OWLClass entityClass = mockOwlEntityAsClass(entity, "http://example.org/entity");

        OWLEntity parent = mock(OWLEntity.class);
        OWLClass parentClass = mockOwlEntityAsClass(parent, "http://example.org/parent");

        ImmutableSet<OWLClass> parents = ImmutableSet.of(parentClass);
        ChangeEntityParentsAction action = new ChangeEntityParentsAction(null, projectId, parents, entityClass, "Test Commit Message");

        @SuppressWarnings("unchecked")
        ChangeApplicationResult<Boolean> mockChangeResult = mock(ChangeApplicationResult.class);
        when(mockChangeResult.getSubject()).thenReturn(true);

        when(changeManager.applyChanges(any(), any())).thenAnswer(invocation -> mockChangeResult);
        when(mockChangeResult.getChangeList()).thenReturn(new ArrayList<>());
        when(classCycleDetector.getClassesWithCycle(any())).thenReturn(Set.of(parentClass));
        OWLEntityData cycleData = mock(OWLEntityData.class);
        when(renderingManager.getRendering(parentClass)).thenReturn(cycleData);

        when(linearizationManager.mergeLinearizationsFromParents(any(), any(), any(), any()))
                .thenReturn(CompletableFuture.completedFuture(new MergeWithParentEntitiesResponse()));

        ChangeEntityParentsResult result = actionHandler.execute(action, executionContext);

        assertNotNull(result);
        assertTrue(result.classesWithCycle().contains(cycleData));
        assertTrue(result.classesWithRetiredParents().isEmpty());
        assertTrue(result.oldParentsThatArelinearizationPathParents().isEmpty());

        verify(classCycleDetector).getClassesWithCycle(any());
    }

    @Test
    @Disabled("Alex Fix this")
    void GIVEN_linearizationPathParents_WHEN_executeCalled_THEN_resultContainsLinearizationPathParents() {
        OWLEntity entity = mock(OWLEntity.class);
        OWLClass entityClass = mockOwlEntityAsClass(entity, "http://example.org/entity");

        IRI linearizationParentIri = IRI.create("http://example.org/linearizationParent");
        Set<IRI> removedParents = Set.of(linearizationParentIri);

        OWLEntity linearizationParent = mock(OWLEntity.class);
        OWLClass linearizationParentClass = mockOwlEntityAsClass(linearizationParent, "http://example.org/linearizationParent");
        OWLEntityData linearizationParentData = mock(OWLEntityData.class);
        when(renderingManager.getRendering(linearizationParentClass)).thenReturn(linearizationParentData);

        ChangeEntityParentsAction action = new ChangeEntityParentsAction(null, projectId, ImmutableSet.of(), entityClass, "Test Commit Message");

        when(linearizationParentChecker.getParentThatIsLinearizationPathParent(entityClass.getIRI(), removedParents, projectId, any(ExecutionContext.class)))
                .thenReturn(Set.of(linearizationParentIri));

        when(classHierarchyProvider.getParents(any())).thenReturn(Set.of(linearizationParentClass));
        when(renderingManager.getRendering((OWLEntity) any())).thenReturn(linearizationParentData);

        ChangeEntityParentsResult result = actionHandler.execute(action, executionContext);

        assertNotNull(result);
        assertTrue(result.oldParentsThatArelinearizationPathParents().contains(linearizationParentData));
        assertTrue(result.classesWithCycle().isEmpty());
        assertTrue(result.classesWithRetiredParents().isEmpty());

        verify(linearizationParentChecker).getParentThatIsLinearizationPathParent(entityClass.getIRI(), removedParents, projectId, new ExecutionContext());
        verify(classCycleDetector, times(0)).getClassesWithCycle(any());
        verify(releasedClassesChecker, times(0)).isReleased(any());
        verify(retiredClassDetector, times(0)).getClassesWithRetiredAncestors(any());
    }

    // Teste unitare pentru metoda createReleasedChildrenValidationMessage
    @Test
    void GIVEN_emptyEntityData_WHEN_createReleasedChildrenValidationMessage_THEN_returnsEmptyString() {
        Set<OWLEntityData> emptyEntityData = Set.of();
        Set<OWLEntityData> parentData = Set.of();
        
        String result = ChangeEntityParentsActionHandler.createReleasedChildrenValidationMessage(
                emptyEntityData, 2, parentData);
        
        assertEquals("", result);
    }

    @Test
    void GIVEN_singleParentAndSingleEntity_WHEN_createReleasedChildrenValidationMessage_THEN_returnsCorrectMessage() {
        OWLEntityData parentData = mock(OWLEntityData.class);
        when(parentData.getBrowserText()).thenReturn("RetiredParent");
        
        OWLEntityData childData = mock(OWLEntityData.class);
        when(childData.getBrowserText()).thenReturn("ReleasedChild");
        
        Set<OWLEntityData> parentSet = Set.of(parentData);
        Set<OWLEntityData> childSet = Set.of(childData);
        
        String result = ChangeEntityParentsActionHandler.createReleasedChildrenValidationMessage(
                childSet, 2, parentSet);
        
        String expectedMessage = "A class that is released or has released descendants cannot be retired!</br>\n" +
                "The following parent has retired ancestors: RetiredParent. </br> The following descendants are released: ReleasedChild.</br> Please correct this in order to save changes";
        
        assertEquals(expectedMessage, result);
    }

    @Test
    void GIVEN_multipleParentsAndSingleEntity_WHEN_createReleasedChildrenValidationMessage_THEN_returnsCorrectMessage() {
        OWLEntityData parent1Data = mock(OWLEntityData.class);
        when(parent1Data.getBrowserText()).thenReturn("RetiredParent1");
        
        OWLEntityData parent2Data = mock(OWLEntityData.class);
        when(parent2Data.getBrowserText()).thenReturn("RetiredParent2");
        
        OWLEntityData childData = mock(OWLEntityData.class);
        when(childData.getBrowserText()).thenReturn("ReleasedChild");
        
        Set<OWLEntityData> parentSet = Set.of(parent1Data, parent2Data);
        Set<OWLEntityData> childSet = Set.of(childData);
        
        String result = ChangeEntityParentsActionHandler.createReleasedChildrenValidationMessage(
                childSet, 2, parentSet);
        
        // Verifică că mesajul conține elementele corecte fără să depindă de ordine
        assertTrue(result.contains("A class that is released or has released descendants cannot be retired!"));
        assertTrue(result.contains("The following parents have retired ancestors:"));
        assertTrue(result.contains("RetiredParent1"));
        assertTrue(result.contains("RetiredParent2"));
        assertTrue(result.contains("The following descendants are released: ReleasedChild"));
        assertTrue(result.contains("Please correct this in order to save changes"));
    }

    @Test
    void GIVEN_singleParentAndMultipleEntitiesWithinLimit_WHEN_createReleasedChildrenValidationMessage_THEN_returnsCorrectMessage() {
        OWLEntityData parentData = mock(OWLEntityData.class);
        when(parentData.getBrowserText()).thenReturn("RetiredParent");
        
        OWLEntityData child1Data = mock(OWLEntityData.class);
        when(child1Data.getBrowserText()).thenReturn("ReleasedChild1");
        
        OWLEntityData child2Data = mock(OWLEntityData.class);
        when(child2Data.getBrowserText()).thenReturn("ReleasedChild2");
        
        Set<OWLEntityData> parentSet = Set.of(parentData);
        Set<OWLEntityData> childSet = Set.of(child1Data, child2Data);
        
        String result = ChangeEntityParentsActionHandler.createReleasedChildrenValidationMessage(
                childSet, 2, parentSet);
        
        // Verifică că mesajul conține elementele corecte fără să depindă de ordine
        assertTrue(result.contains("A class that is released or has released descendants cannot be retired!"));
        assertTrue(result.contains("The following parent has retired ancestors: RetiredParent"));
        assertTrue(result.contains("The following descendants are released:"));
        assertTrue(result.contains("ReleasedChild1"));
        assertTrue(result.contains("ReleasedChild2"));
        assertTrue(result.contains("Please correct this in order to save changes"));
        
        // Verifică că mesajul conține ambele entități
        assertTrue(result.contains("ReleasedChild1"));
        assertTrue(result.contains("ReleasedChild2"));
    }

    @Test
    void GIVEN_singleParentAndMultipleEntitiesOverLimit_WHEN_createReleasedChildrenValidationMessage_THEN_returnsCorrectMessage() {
        OWLEntityData parentData = mock(OWLEntityData.class);
        when(parentData.getBrowserText()).thenReturn("RetiredParent");
        
        OWLEntityData child1Data = mock(OWLEntityData.class);
        when(child1Data.getBrowserText()).thenReturn("ReleasedChild1");
        
        OWLEntityData child2Data = mock(OWLEntityData.class);
        when(child2Data.getBrowserText()).thenReturn("ReleasedChild2");
        
        OWLEntityData child3Data = mock(OWLEntityData.class);
        when(child3Data.getBrowserText()).thenReturn("ReleasedChild3");
        
        Set<OWLEntityData> parentSet = Set.of(parentData);
        Set<OWLEntityData> childSet = Set.of(child1Data, child2Data, child3Data);
        
        String result = ChangeEntityParentsActionHandler.createReleasedChildrenValidationMessage(
                childSet, 2, parentSet);
        
        // Verifică că mesajul conține elementele corecte fără să depindă de ordine
        assertTrue(result.contains("A class that is released or has released descendants cannot be retired!"));
        assertTrue(result.contains("The following parent has retired ancestors: RetiredParent"));
        assertTrue(result.contains("The following descendants are released:"));
        assertTrue(result.contains("and 1 more entities"));
        assertTrue(result.contains("Please correct this in order to save changes"));
        
        // Verifică că sunt afișate exact 2 entități plus "and 1 more"
        String descendantsPart = result.split("The following descendants are released: ")[1].split(" and ")[0];
        String[] entityNames = descendantsPart.split(", ");
        assertEquals(2, entityNames.length);
    }

    @Test
    void GIVEN_multipleParentsAndMultipleEntitiesOverLimit_WHEN_createReleasedChildrenValidationMessage_THEN_returnsCorrectMessage() {
        OWLEntityData parent1Data = mock(OWLEntityData.class);
        when(parent1Data.getBrowserText()).thenReturn("RetiredParent1");
        
        OWLEntityData parent2Data = mock(OWLEntityData.class);
        when(parent2Data.getBrowserText()).thenReturn("RetiredParent2");
        
        OWLEntityData child1Data = mock(OWLEntityData.class);
        when(child1Data.getBrowserText()).thenReturn("ReleasedChild1");
        
        OWLEntityData child2Data = mock(OWLEntityData.class);
        when(child2Data.getBrowserText()).thenReturn("ReleasedChild2");
        
        OWLEntityData child3Data = mock(OWLEntityData.class);
        when(child3Data.getBrowserText()).thenReturn("ReleasedChild3");
        
        OWLEntityData child4Data = mock(OWLEntityData.class);
        when(child4Data.getBrowserText()).thenReturn("ReleasedChild4");
        
        Set<OWLEntityData> parentSet = Set.of(parent1Data, parent2Data);
        Set<OWLEntityData> childSet = Set.of(child1Data, child2Data, child3Data, child4Data);
        
        String result = ChangeEntityParentsActionHandler.createReleasedChildrenValidationMessage(
                childSet, 2, parentSet);
        
        // Verifică că mesajul conține elementele corecte fără să depindă de ordine
        assertTrue(result.contains("A class that is released or has released descendants cannot be retired!"));
        assertTrue(result.contains("The following parents have retired ancestors:"));
        assertTrue(result.contains("RetiredParent1"));
        assertTrue(result.contains("RetiredParent2"));
        assertTrue(result.contains("The following descendants are released:"));
        assertTrue(result.contains("and 2 more entities"));
        assertTrue(result.contains("Please correct this in order to save changes"));
        
        // Verifică că sunt afișate exact 2 entități plus "and 2 more"
        String descendantsPart = result.split("The following descendants are released: ")[1].split(" and ")[0];
        String[] entityNames = descendantsPart.split(", ");
        assertEquals(2, entityNames.length);
    }

    @Test
    void GIVEN_entitiesToShowZero_WHEN_createReleasedChildrenValidationMessage_THEN_returnsCorrectMessage() {
        OWLEntityData parentData = mock(OWLEntityData.class);
        when(parentData.getBrowserText()).thenReturn("RetiredParent");
        
        OWLEntityData child1Data = mock(OWLEntityData.class);
        when(child1Data.getBrowserText()).thenReturn("ReleasedChild1");
        
        OWLEntityData child2Data = mock(OWLEntityData.class);
        when(child2Data.getBrowserText()).thenReturn("ReleasedChild2");
        
        Set<OWLEntityData> parentSet = Set.of(parentData);
        Set<OWLEntityData> childSet = Set.of(child1Data, child2Data);
        
        String result = ChangeEntityParentsActionHandler.createReleasedChildrenValidationMessage(
                childSet, 0, parentSet);
        
        String expectedMessage = "A class that is released or has released descendants cannot be retired!</br>\n" +
                "The following parent has retired ancestors: RetiredParent. </br> The following descendants are released:  and 2 more entities.</br> Please correct this in order to save changes";
        
        assertEquals(expectedMessage, result);
    }
}
