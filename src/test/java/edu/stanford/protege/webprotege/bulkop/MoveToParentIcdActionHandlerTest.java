package edu.stanford.protege.webprotege.bulkop;

import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.entity.OWLEntityData;
import edu.stanford.protege.webprotege.hierarchy.ClassHierarchyProvider;
import edu.stanford.protege.webprotege.icd.LinearizationParentChecker;
import edu.stanford.protege.webprotege.icd.ReleasedClassesChecker;
import edu.stanford.protege.webprotege.icd.hierarchy.ClassHierarchyRetiredClassDetector;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.linearization.LinearizationManager;
import edu.stanford.protege.webprotege.linearization.MergeWithParentEntitiesResponse;
import edu.stanford.protege.webprotege.project.chg.ChangeManager;
import edu.stanford.protege.webprotege.renderer.RenderingManager;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MoveToParentIcdActionHandlerTest {

    @Mock
    private AccessManager accessManager;

    @Mock
    private MoveClassesChangeListGeneratorFactory factory;

    @Mock
    private ReleasedClassesChecker releasedClassesChecker;

    @Mock
    private ClassHierarchyRetiredClassDetector retiredAncestorDetector;

    @Mock
    private ChangeManager changeManager;

    @Mock
    private LinearizationManager linearizationManager;

    @Mock
    private LinearizationParentChecker linParentChecker;

    @Mock
    private RenderingManager renderingManager;

    @Mock
    private ClassHierarchyProvider classHierarchyProvider;

    private ProjectId projectId;
    private MoveToParentIcdActionHandler actionHandler;
    private ExecutionContext executionContext;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        projectId = ProjectId.generate();
        executionContext = new ExecutionContext(UserId.getGuest(), "");
        actionHandler = new MoveToParentIcdActionHandler(accessManager,
                factory,
                releasedClassesChecker,
                retiredAncestorDetector,
                changeManager,
                linearizationManager,
                linParentChecker,
                renderingManager,
                classHierarchyProvider);
    }

    private OWLClass mockOwlEntityAsClass(OWLEntity entity, String iri) {
        OWLClass owlClass = mock(OWLClass.class);
        IRI entityIri = IRI.create(iri);
        when(entity.getIRI()).thenReturn(entityIri);
        when(entity.asOWLClass()).thenReturn(owlClass);
        when(owlClass.getIRI()).thenReturn(entityIri);
        when(owlClass.isOWLClass()).thenReturn(true);
        when(owlClass.asOWLClass()).thenReturn(owlClass);
        when(owlClass.toStringID()).thenReturn(iri);
        return owlClass;
    }

    @Test
    void GIVEN_nonOwlClassParent_WHEN_executeCalled_THEN_returnsFailure() {

        OWLEntity entity = mock(OWLEntity.class);
        OWLClass entityClass = mockOwlEntityAsClass(entity, "http://example.org/entity");
        OWLEntity parent = mock(OWLEntity.class);
        OWLClass parentClass = mockOwlEntityAsClass(parent, "http://example.org/parent");
        MoveEntitiesToParentIcdAction action = new MoveEntitiesToParentIcdAction(null, projectId, ImmutableSet.of(entityClass), parentClass, "Test Commit Message");

        when(parent.isOWLClass()).thenReturn(false);

        MoveEntitiesToParentIcdResult result = actionHandler.execute(action, executionContext);

        assertFalse(result.isDestinationRetiredClass());
        assertTrue(result.entitiesForWhichParentIsLinPathParent().isEmpty());
    }

    @Test
    void GIVEN_retiredParent_WHEN_executeCalled_THEN_returnsFailure() {
        OWLEntity entity = mock(OWLEntity.class);
        OWLClass entityClass = mockOwlEntityAsClass(entity, "http://example.org/entity");
        OWLEntity parent = mock(OWLEntity.class);
        OWLClass parentClass = mockOwlEntityAsClass(parent, "http://example.org/parent");
        when(releasedClassesChecker.isReleased(entityClass)).thenReturn(true);
        when(retiredAncestorDetector.isRetired(parentClass)).thenReturn(true);

        when(linearizationManager.mergeLinearizationsFromParents(any(), any(), any(), any()))
                .thenReturn(CompletableFuture.completedFuture(new MergeWithParentEntitiesResponse()));

        MoveEntitiesToParentIcdAction action = new MoveEntitiesToParentIcdAction(null, projectId, ImmutableSet.of(entityClass), parentClass, "Test Commit Message");

        MoveEntitiesToParentIcdResult result = actionHandler.execute(action, executionContext);

        assertTrue(result.isDestinationRetiredClass());
        assertTrue(result.entitiesForWhichParentIsLinPathParent().isEmpty());
    }

    @Test
    void GIVEN_validMove_WHEN_executeCalled_THEN_success() {
        OWLEntity entity = mock(OWLEntity.class);
        OWLClass parentClass = mockOwlEntityAsClass(entity, "http://example.org/parent");

        when(releasedClassesChecker.isReleased(entity)).thenReturn(false);

        MoveEntitiesToParentIcdAction action = new MoveEntitiesToParentIcdAction(null, projectId, ImmutableSet.of(mockOwlEntityAsClass(entity, "http://example.org/entity")), parentClass, "Test Commit Message");

        when(changeManager.applyChanges(any(), any())).thenReturn(null);
        when(linearizationManager.mergeLinearizationsFromParents(any(), any(), any(), any()))
                .thenReturn(CompletableFuture.completedFuture(null));

        MoveEntitiesToParentIcdResult result = actionHandler.execute(action, executionContext);

        assertFalse(result.isDestinationRetiredClass());
        assertTrue(result.entitiesForWhichParentIsLinPathParent().isEmpty());
    }

    @Test
    void GIVEN_linearizationPathParents_WHEN_executeCalled_THEN_resultContainsLinearizationPathParents() {
        OWLEntity entity = mock(OWLEntity.class);
        OWLEntity newParent = mock(OWLEntity.class);
        OWLEntity currParent = mock(OWLEntity.class);
        String currentParentIri = "http://example.org/parent";
        OWLClass entityClass = mockOwlEntityAsClass(entity, "http://example.org/entity");
        OWLClass currParentClass = mockOwlEntityAsClass(currParent, currentParentIri);
        OWLClass newParentClass = mockOwlEntityAsClass(currParent, currentParentIri);

        when(classHierarchyProvider.getParents(any())).thenReturn(Set.of(currParentClass));

        OWLEntityData parentEntityData = mock(OWLEntityData.class);
        String parentBrowserData = currParentClass.toStringID();
        when(parentEntityData.getBrowserText()).thenReturn(parentBrowserData);
        when(renderingManager.getRendering((OWLEntity) any())).thenReturn(parentEntityData);


        MoveEntitiesToParentIcdAction action = new MoveEntitiesToParentIcdAction(null, projectId, ImmutableSet.of(entityClass), newParentClass, "Test Commit Message");
        IRI currParentIri = currParentClass.getIRI();
        when(linParentChecker.getParentThatIsLinearizationPathParent(any(), any(), any())).thenReturn(Set.of(currParentIri));


        Map<String, ImmutableSet<OWLEntityData>> expectedMap = new HashMap<>();
        expectedMap.put(currParentClass.toStringID(), ImmutableSet.of(parentEntityData));

        MoveEntitiesToParentIcdResult result = actionHandler.execute(action, executionContext);

        assertFalse(result.isDestinationRetiredClass());
        assertEquals(1, result.entitiesForWhichParentIsLinPathParent().size());
        verify(linParentChecker).getParentThatIsLinearizationPathParent(entityClass.getIRI(), Set.of(currParentClass.getIRI()), projectId);
    }
}
