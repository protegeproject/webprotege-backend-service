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
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.linearization.LinearizationManager;
import edu.stanford.protege.webprotege.linearization.MergeWithParentEntitiesResponse;
import edu.stanford.protege.webprotege.project.chg.ChangeManager;
import edu.stanford.protege.webprotege.renderer.RenderingManager;
import edu.stanford.protege.webprotege.revision.Revision;
import edu.stanford.protege.webprotege.revision.RevisionManager;
import edu.stanford.protege.webprotege.revision.RevisionNumber;
import org.junit.jupiter.api.BeforeEach;
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
                projectOrderedChildrenManager);
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
        when(retiredClassDetector.getClassesWithRetiredAncestors(any())).thenReturn(Set.of(parentClass));
        OWLEntityData retiredParentData = mock(OWLEntityData.class);
        when(renderingManager.getRendering(parentClass)).thenReturn(retiredParentData);

        ChangeApplicationResult<Boolean> mockChangeResult = mock(ChangeApplicationResult.class);
        when(mockChangeResult.getChangeList()).thenReturn(new ArrayList<>());

        when(changeManager.applyChanges(any(), any())).thenAnswer(invocation -> mockChangeResult);

        when(linearizationManager.mergeLinearizationsFromParents(any(), any(), any(), any()))
                .thenReturn(CompletableFuture.completedFuture(new MergeWithParentEntitiesResponse()));

        ChangeEntityParentsResult result = actionHandler.execute(action, executionContext);

        assertNotNull(result);
        assertTrue(result.classesWithRetiredParents().contains(retiredParentData));
        assertTrue(result.classesWithCycle().isEmpty());
        assertTrue(result.oldParentsThatArelinearizationPathParents().isEmpty());

        verify(retiredClassDetector).getClassesWithRetiredAncestors(parents);
    }

    @Test
    void GIVEN_cyclesDetected_WHEN_executeCalled_THEN_resultContainsCycles() {
        OWLEntity entity = mock(OWLEntity.class);
        OWLClass entityClass = mockOwlEntityAsClass(entity, "http://example.org/entity");

        OWLEntity parent = mock(OWLEntity.class);
        OWLClass parentClass = mockOwlEntityAsClass(parent, "http://example.org/parent");

        ImmutableSet<OWLClass> parents = ImmutableSet.of(parentClass);
        ChangeEntityParentsAction action = new ChangeEntityParentsAction(null, projectId, parents, entityClass, "Test Commit Message");

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

        when(linearizationParentChecker.getParentThatIsLinearizationPathParent(entityClass.getIRI(), removedParents, projectId, new ExecutionContext()))
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
}
