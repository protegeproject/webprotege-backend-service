package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.change.ChangeApplicationResult;
import edu.stanford.protege.webprotege.change.OntologyChange;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.entity.EntityNode;
import edu.stanford.protege.webprotege.hierarchy.ordering.ProjectOrderedChildrenManager;
import edu.stanford.protege.webprotege.icd.LinearizationParentChecker;
import edu.stanford.protege.webprotege.icd.ReleasedClassesChecker;
import edu.stanford.protege.webprotege.icd.hierarchy.ClassHierarchyRetiredClassDetector;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.linearization.LinearizationManager;
import edu.stanford.protege.webprotege.owlapi.RenameMap;
import edu.stanford.protege.webprotege.project.chg.ChangeManager;
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

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MoveHierarchyNodeIcdActionHandlerTest {

    @Mock
    private AccessManager accessManager;

    @Mock
    private MoveEntityChangeListGeneratorFactory factory;

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

    private ProjectId projectId;
    private MoveHierarchyNodeIcdActionHandler actionHandler;
    private ExecutionContext executionContext;

    @Mock
    private ChangeApplicationResult<Boolean> changeApplicationResult;
    @Mock
    private List<OntologyChange> ontologyChangeList;
    @Mock
    private RenameMap renameMap;

    @Mock
    private ProjectOrderedChildrenManager projectOrderedChildrenManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        projectId = ProjectId.generate();
        executionContext = new ExecutionContext(UserId.getGuest(), "", UUID.randomUUID().toString());

        actionHandler = new MoveHierarchyNodeIcdActionHandler(
                accessManager,
                factory,
                releasedClassesChecker,
                retiredAncestorDetector,
                changeManager,
                linearizationManager,
                linParentChecker,
                projectOrderedChildrenManager);

        when(changeApplicationResult.getRenameMap()).thenReturn(renameMap);
        when(changeApplicationResult.getChangeList()).thenReturn(ontologyChangeList);
        when(changeApplicationResult.getSubject()).thenReturn(true);
    }

    @Test
    void GIVEN_retiredParent_WHEN_executeCalled_THEN_returnsFailure() {
        Path<EntityNode> fromNodePath = mock(Path.class);
        Path<EntityNode> toNodeParentPath = mock(Path.class);
        MoveHierarchyNodeIcdAction action = new MoveHierarchyNodeIcdAction(null,
                projectId,
                ClassHierarchyDescriptor.create(),
                fromNodePath,
                toNodeParentPath,
                DropType.MOVE);

        var entityNode = mock(EntityNode.class);
        var owlEntity = mock(OWLEntity.class);
        var owlClass = mock(OWLClass.class);
        when(fromNodePath.getLast()).thenReturn(java.util.Optional.of(entityNode));
        when(entityNode.getEntity()).thenReturn(owlEntity);
        when(owlEntity.isOWLClass()).thenReturn(true);
        when(owlEntity.asOWLClass()).thenReturn(owlClass);

        var destinationNode = mock(EntityNode.class);
        var destinationEntity = mock(OWLEntity.class);
        var destinationClass = mock(OWLClass.class);
        when(toNodeParentPath.getLast()).thenReturn(java.util.Optional.of(destinationNode));
        when(destinationNode.getEntity()).thenReturn(destinationEntity);
        when(destinationEntity.isOWLClass()).thenReturn(true);
        when(destinationEntity.asOWLClass()).thenReturn(destinationClass);


        when(releasedClassesChecker.isReleased(any())).thenReturn(true);
        when(retiredAncestorDetector.isRetired(any())).thenReturn(true);

        MoveHierarchyNodeIcdResult result = actionHandler.execute(action, executionContext);

        assertFalse(result.moved());
        assertTrue(result.isDestinationRetiredClass());
        assertFalse(result.isInitialParentLinPathParent());
    }

    @Test
    void GIVEN_validMove_WHEN_executeCalled_THEN_success() {
        Path<EntityNode> fromNodePath = mock(Path.class);
        Path<EntityNode> toNodeParentPath = mock(Path.class);
        MoveHierarchyNodeIcdAction action = new MoveHierarchyNodeIcdAction(null,
                projectId,
                ClassHierarchyDescriptor.create(),
                fromNodePath,
                toNodeParentPath,
                DropType.MOVE);

        var previousParent = mock(EntityNode.class);
        var previousParentEntity = mock(OWLEntity.class);
        var previousParentIri = IRI.create("http://example.org/previousParent");
        when(fromNodePath.getLastPredecessor()).thenReturn(java.util.Optional.of(previousParent));
        when(previousParent.getEntity()).thenReturn(previousParentEntity);
        when(previousParentEntity.getIRI()).thenReturn(previousParentIri);

        var entityNode = mock(EntityNode.class);
        var entity = mock(OWLEntity.class);
        var sourceIri = IRI.create("http://example.org/destination");
        when(fromNodePath.getLast()).thenReturn(java.util.Optional.of(entityNode));
        when(entityNode.getEntity()).thenReturn(entity);
        when(entity.isOWLClass()).thenReturn(true);
        when(entity.getIRI()).thenReturn(sourceIri);

        var destinationNode = mock(EntityNode.class);
        var destinationEntity = mock(OWLEntity.class);
        var destinationClass = mock(OWLClass.class);
        var destinationIri = IRI.create("http://example.org/destination");
        when(toNodeParentPath.getLast()).thenReturn(java.util.Optional.of(destinationNode));
        when(destinationNode.getEntity()).thenReturn(destinationEntity);
        when(destinationEntity.isOWLClass()).thenReturn(true);
        when(destinationEntity.asOWLClass()).thenReturn(destinationClass);
        when(destinationEntity.getIRI()).thenReturn(destinationIri);

        when(releasedClassesChecker.isReleased(entity)).thenReturn(false);
        doReturn(changeApplicationResult).when(changeManager).applyChanges(any(), any());
        when(linearizationManager.mergeLinearizationsFromParents(any(), any(), any(), any()))
                .thenReturn(CompletableFuture.completedFuture(null));

        MoveHierarchyNodeIcdResult result = actionHandler.execute(action, executionContext);

        assertTrue(result.moved());
        assertFalse(result.isDestinationRetiredClass());
        assertFalse(result.isInitialParentLinPathParent());

        verify(linearizationManager).mergeLinearizationsFromParents(
                entity.getIRI(),
                Set.of(destinationEntity.getIRI()),
                action.projectId(),
                executionContext
        );
    }


    @Test
    @Disabled("Alex Fix this")
    void GIVEN_linearizationPathParents_WHEN_executeCalled_THEN_resultContainsLinearizationPathParents() {
        Path<EntityNode> fromNodePath = mock(Path.class);
        Path<EntityNode> toNodeParentPath = mock(Path.class);
        MoveHierarchyNodeIcdAction action = new MoveHierarchyNodeIcdAction(null,
                projectId,
                ClassHierarchyDescriptor.create(),
                fromNodePath,
                toNodeParentPath,
                DropType.MOVE);

        var entityNode = mock(EntityNode.class);
        var entity = mock(OWLEntity.class);
        var entityIri = IRI.create("http://example.org/entity");
        when(fromNodePath.getLast()).thenReturn(java.util.Optional.of(entityNode));
        when(entityNode.getEntity()).thenReturn(entity);
        when(entity.isOWLClass()).thenReturn(true);
        when(entity.getIRI()).thenReturn(entityIri);

        var destinationNode = mock(EntityNode.class);
        var destinationEntity = mock(OWLEntity.class);
        var destinationIri = IRI.create("http://example.org/destination");
        when(toNodeParentPath.getLast()).thenReturn(java.util.Optional.of(destinationNode));
        when(destinationNode.getEntity()).thenReturn(destinationEntity);
        when(destinationEntity.getIRI()).thenReturn(destinationIri);

        var previousParent = mock(EntityNode.class);
        var previousParentEntity = mock(OWLEntity.class);
        var previousParentIri = IRI.create("http://example.org/previousParent");
        when(fromNodePath.getLastPredecessor()).thenReturn(java.util.Optional.of(previousParent));
        when(previousParent.getEntity()).thenReturn(previousParentEntity);
        when(previousParentEntity.getIRI()).thenReturn(previousParentIri);

        when(linParentChecker.getParentThatIsLinearizationPathParent(entityIri,
                Set.of(previousParentIri),
                action.projectId(), new ExecutionContext())
        ).thenReturn(Set.of(previousParentIri));

        MoveHierarchyNodeIcdResult result = actionHandler.execute(action, executionContext);

        assertFalse(result.moved());
        assertFalse(result.isDestinationRetiredClass());
        assertTrue(result.isInitialParentLinPathParent());

        verify(linParentChecker).getParentThatIsLinearizationPathParent(
                entityIri,
                Set.of(previousParentIri),
                action.projectId(), new ExecutionContext()
        );
    }

}
