package edu.stanford.protege.webprotege.icd.actions;

import edu.stanford.protege.webprotege.DataFactory;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.hierarchy.*;
import edu.stanford.protege.webprotege.ipc.CommandExecutor;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.postcoordination.GetPostcoordinationAxisToGenericScaleRequest;
import edu.stanford.protege.webprotege.postcoordination.GetPostcoordinationAxisToGenericScaleResponse;
import edu.stanford.protege.webprotege.postcoordination.PostcoordinationAxisToGenericScale;
import edu.stanford.protege.webprotege.renderer.RenderingManager;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ValidateLogicalDefinitionsFromApiActionHandler_TestCase {

    private ValidateLogicalDefinitionsFromApiActionHandler handler;

    @Mock
    private AccessManager accessManager;

    @Mock
    private CommandExecutor<GetPostcoordinationAxisToGenericScaleRequest, GetPostcoordinationAxisToGenericScaleResponse> getPostcoordinationAxisToGenericScaleExecutor;

    @Mock
    private HierarchyProviderManager hierarchyProviderManager;

    @Mock
    private ClassHierarchyProvider classHierarchyProvider;

    @Mock
    private RenderingManager renderingManager;

    @Mock
    private HierarchyProvider<OWLEntity> hierarchyProvider;

    private ProjectId projectId = ProjectId.generate();
    private ExecutionContext executionContext = new ExecutionContext(new UserId("1"), "DUMMY_JWT", "correlationId");

    private static final String ENTITY_IRI = "http://example.org/Entity";
    private static final String AXIS_IRI = "http://example.org/Axis";
    private static final String FILLER_IRI = "http://example.org/Filler";
    private static final String INVALID_FILLER_IRI = "http://example.org/InvalidFiller";
    private static final String SUPERCLASS_IRI = "http://example.org/Superclass";
    private static final String GENERIC_SCALE_TOP_CLASS_IRI = "http://example.org/GenericScaleTopClass";

    @BeforeEach
    public void setUp() {
        handler = new ValidateLogicalDefinitionsFromApiActionHandler(
                accessManager,
                getPostcoordinationAxisToGenericScaleExecutor,
                hierarchyProviderManager,
                classHierarchyProvider,
                renderingManager
        );
    }

    @Test
    public void shouldThrowNullPointerExceptionIf_accessManager_IsNull() {
        assertThrows(NullPointerException.class, () -> {
            new ValidateLogicalDefinitionsFromApiActionHandler(
                    null,
                    getPostcoordinationAxisToGenericScaleExecutor,
                    hierarchyProviderManager,
                    classHierarchyProvider,
                    renderingManager
            );
        });
    }

    @Test
    public void shouldImplementToString() {
        assertThat(handler.toString(), Matchers.containsString("ValidateLogicalDefinitionsFromApiActionHandler"));
    }

    @Test
    public void should_getActionClass() {
        assertThat(handler.getActionClass(), is(ValidateLogicalDefinitionsFromApiAction.class));
    }

    @Test
    public void should_execute_withNoErrors_whenAllValidationsPass() throws Exception {
        // Setup
        ValidateLogicalDefinitionsFromApiAction action = createValidAction();
        
        PostcoordinationAxisToGenericScale axisToScale = new PostcoordinationAxisToGenericScale(
                AXIS_IRI,
                GENERIC_SCALE_TOP_CLASS_IRI,
                "false"
        );
        GetPostcoordinationAxisToGenericScaleResponse response = GetPostcoordinationAxisToGenericScaleResponse.create(
                List.of(axisToScale)
        );
        
        CompletableFuture<GetPostcoordinationAxisToGenericScaleResponse> future = 
                CompletableFuture.completedFuture(response);
        when(getPostcoordinationAxisToGenericScaleExecutor.execute(any(), any())).thenReturn(future);

        OWLClass rootClass = DataFactory.getOWLClass(IRI.create(GENERIC_SCALE_TOP_CLASS_IRI));
        OWLClass fillerClass = DataFactory.getOWLClass(IRI.create(FILLER_IRI));
        OWLClass entityClass = DataFactory.getOWLClass(IRI.create(ENTITY_IRI));
        OWLClass superclassClass = DataFactory.getOWLClass(IRI.create(SUPERCLASS_IRI));

        ClassHierarchyDescriptor hierarchyDescriptor = ClassHierarchyDescriptor.create(Set.of(rootClass));
        when(hierarchyProviderManager.getHierarchyProvider(hierarchyDescriptor))
                .thenReturn(Optional.of(hierarchyProvider));
        when(hierarchyProvider.getDescendants(rootClass)).thenReturn(Set.of(fillerClass));
        when(classHierarchyProvider.isAncestor(entityClass, superclassClass)).thenReturn(true);
        when(renderingManager.getShortForm(any(OWLClass.class))).thenReturn("TestClass");

        // Execute
        ValidateLogicalDefinitionsFromApiResult result = handler.execute(action, executionContext);

        // Verify
        assertThat(result.messages(), is(empty()));
        verify(getPostcoordinationAxisToGenericScaleExecutor).execute(any(), any());
        verify(hierarchyProviderManager).getHierarchyProvider(any());
        verify(classHierarchyProvider).isAncestor(entityClass, superclassClass);
    }

    @Test
    public void should_execute_withError_whenFillerNotInHierarchy() throws Exception {
        // Setup
        ValidateLogicalDefinitionsFromApiAction action = new ValidateLogicalDefinitionsFromApiAction(
                projectId,
                ENTITY_IRI,
                List.of(new ValidateLogicalDefinitionsFromApiAction.LogicalDefinition(
                        SUPERCLASS_IRI,
                        List.of(new ValidateLogicalDefinitionsFromApiAction.Relationship(
                                AXIS_IRI,
                                INVALID_FILLER_IRI
                        ))
                )),
                List.of()
        );

        PostcoordinationAxisToGenericScale axisToScale = new PostcoordinationAxisToGenericScale(
                AXIS_IRI,
                GENERIC_SCALE_TOP_CLASS_IRI,
                "false"
        );
        GetPostcoordinationAxisToGenericScaleResponse response = GetPostcoordinationAxisToGenericScaleResponse.create(
                List.of(axisToScale)
        );
        
        CompletableFuture<GetPostcoordinationAxisToGenericScaleResponse> future = 
                CompletableFuture.completedFuture(response);
        when(getPostcoordinationAxisToGenericScaleExecutor.execute(any(), any())).thenReturn(future);

        OWLClass rootClass = DataFactory.getOWLClass(IRI.create(GENERIC_SCALE_TOP_CLASS_IRI));
        OWLClass invalidFillerClass = DataFactory.getOWLClass(IRI.create(INVALID_FILLER_IRI));
        OWLClass axisClass = DataFactory.getOWLClass(IRI.create(AXIS_IRI));
        OWLClass entityClass = DataFactory.getOWLClass(IRI.create(ENTITY_IRI));
        OWLClass superclassClass = DataFactory.getOWLClass(IRI.create(SUPERCLASS_IRI));

        ClassHierarchyDescriptor hierarchyDescriptor = ClassHierarchyDescriptor.create(Set.of(rootClass));
        when(hierarchyProviderManager.getHierarchyProvider(hierarchyDescriptor))
                .thenReturn(Optional.of(hierarchyProvider));
        when(hierarchyProvider.getDescendants(rootClass)).thenReturn(Set.of()); // Empty - filler not in hierarchy
        when(classHierarchyProvider.isAncestor(entityClass, superclassClass)).thenReturn(true);
        when(renderingManager.getShortForm(invalidFillerClass)).thenReturn("InvalidFiller");
        when(renderingManager.getShortForm(axisClass)).thenReturn("Axis");
        when(renderingManager.getShortForm(rootClass)).thenReturn("GenericScaleTopClass");

        // Execute
        ValidateLogicalDefinitionsFromApiResult result = handler.execute(action, executionContext);

        // Verify
        assertThat(result.messages(), hasSize(1));
        assertThat(result.messages().get(0), containsString("Filler 'InvalidFiller' is not valid for axis 'Axis'"));
        assertThat(result.messages().get(0), containsString("It must be a descendant of 'GenericScaleTopClass'"));
    }

    @Test
    public void should_execute_withError_whenNecessaryConditionFillerNotInHierarchy() throws Exception {
        // Setup
        ValidateLogicalDefinitionsFromApiAction action = new ValidateLogicalDefinitionsFromApiAction(
                projectId,
                ENTITY_IRI,
                List.of(),
                List.of(new ValidateLogicalDefinitionsFromApiAction.Relationship(
                        AXIS_IRI,
                        INVALID_FILLER_IRI
                ))
        );

        PostcoordinationAxisToGenericScale axisToScale = new PostcoordinationAxisToGenericScale(
                AXIS_IRI,
                GENERIC_SCALE_TOP_CLASS_IRI,
                "false"
        );
        GetPostcoordinationAxisToGenericScaleResponse response = GetPostcoordinationAxisToGenericScaleResponse.create(
                List.of(axisToScale)
        );
        
        CompletableFuture<GetPostcoordinationAxisToGenericScaleResponse> future = 
                CompletableFuture.completedFuture(response);
        when(getPostcoordinationAxisToGenericScaleExecutor.execute(any(), any())).thenReturn(future);

        OWLClass rootClass = DataFactory.getOWLClass(IRI.create(GENERIC_SCALE_TOP_CLASS_IRI));
        OWLClass invalidFillerClass = DataFactory.getOWLClass(IRI.create(INVALID_FILLER_IRI));
        OWLClass axisClass = DataFactory.getOWLClass(IRI.create(AXIS_IRI));

        ClassHierarchyDescriptor hierarchyDescriptor = ClassHierarchyDescriptor.create(Set.of(rootClass));
        when(hierarchyProviderManager.getHierarchyProvider(hierarchyDescriptor))
                .thenReturn(Optional.of(hierarchyProvider));
        when(hierarchyProvider.getDescendants(rootClass)).thenReturn(Set.of()); // Empty - filler not in hierarchy
        when(renderingManager.getShortForm(invalidFillerClass)).thenReturn("InvalidFiller");
        when(renderingManager.getShortForm(axisClass)).thenReturn("Axis");
        when(renderingManager.getShortForm(rootClass)).thenReturn("GenericScaleTopClass");

        // Execute
        ValidateLogicalDefinitionsFromApiResult result = handler.execute(action, executionContext);

        // Verify
        assertThat(result.messages(), hasSize(1));
        assertThat(result.messages().get(0), containsString("Filler 'InvalidFiller' is not valid for axis 'Axis'"));
    }

    @Test
    public void should_execute_withError_whenSuperclassNotAncestor() throws Exception {
        // Setup
        ValidateLogicalDefinitionsFromApiAction action = new ValidateLogicalDefinitionsFromApiAction(
                projectId,
                ENTITY_IRI,
                List.of(new ValidateLogicalDefinitionsFromApiAction.LogicalDefinition(
                        SUPERCLASS_IRI,
                        List.of()
                )),
                List.of()
        );

        GetPostcoordinationAxisToGenericScaleResponse response = GetPostcoordinationAxisToGenericScaleResponse.create(
                List.of()
        );
        
        CompletableFuture<GetPostcoordinationAxisToGenericScaleResponse> future = 
                CompletableFuture.completedFuture(response);
        when(getPostcoordinationAxisToGenericScaleExecutor.execute(any(), any())).thenReturn(future);

        OWLClass entityClass = DataFactory.getOWLClass(IRI.create(ENTITY_IRI));
        OWLClass superclassClass = DataFactory.getOWLClass(IRI.create(SUPERCLASS_IRI));

        when(classHierarchyProvider.isAncestor(entityClass, superclassClass)).thenReturn(false);
        when(renderingManager.getShortForm(superclassClass)).thenReturn("Superclass");
        when(renderingManager.getShortForm(entityClass)).thenReturn("Entity");

        // Execute
        ValidateLogicalDefinitionsFromApiResult result = handler.execute(action, executionContext);

        // Verify
        assertThat(result.messages(), hasSize(1));
        assertThat(result.messages().get(0), containsString("Logical definition superclass 'Superclass' is not an ancestor of entity 'Entity'"));
    }

    @Test
    public void should_execute_withMultipleErrors() throws Exception {
        // Setup - invalid filler and invalid superclass
        ValidateLogicalDefinitionsFromApiAction action = new ValidateLogicalDefinitionsFromApiAction(
                projectId,
                ENTITY_IRI,
                List.of(new ValidateLogicalDefinitionsFromApiAction.LogicalDefinition(
                        SUPERCLASS_IRI,
                        List.of(new ValidateLogicalDefinitionsFromApiAction.Relationship(
                                AXIS_IRI,
                                INVALID_FILLER_IRI
                        ))
                )),
                List.of()
        );

        PostcoordinationAxisToGenericScale axisToScale = new PostcoordinationAxisToGenericScale(
                AXIS_IRI,
                GENERIC_SCALE_TOP_CLASS_IRI,
                "false"
        );
        GetPostcoordinationAxisToGenericScaleResponse response = GetPostcoordinationAxisToGenericScaleResponse.create(
                List.of(axisToScale)
        );
        
        CompletableFuture<GetPostcoordinationAxisToGenericScaleResponse> future = 
                CompletableFuture.completedFuture(response);
        when(getPostcoordinationAxisToGenericScaleExecutor.execute(any(), any())).thenReturn(future);

        OWLClass rootClass = DataFactory.getOWLClass(IRI.create(GENERIC_SCALE_TOP_CLASS_IRI));
        OWLClass invalidFillerClass = DataFactory.getOWLClass(IRI.create(INVALID_FILLER_IRI));
        OWLClass axisClass = DataFactory.getOWLClass(IRI.create(AXIS_IRI));
        OWLClass entityClass = DataFactory.getOWLClass(IRI.create(ENTITY_IRI));
        OWLClass superclassClass = DataFactory.getOWLClass(IRI.create(SUPERCLASS_IRI));

        ClassHierarchyDescriptor hierarchyDescriptor = ClassHierarchyDescriptor.create(Set.of(rootClass));
        when(hierarchyProviderManager.getHierarchyProvider(hierarchyDescriptor))
                .thenReturn(Optional.of(hierarchyProvider));
        when(hierarchyProvider.getDescendants(rootClass)).thenReturn(Set.of()); // Empty - filler not in hierarchy
        when(classHierarchyProvider.isAncestor(entityClass, superclassClass)).thenReturn(false);
        when(renderingManager.getShortForm(invalidFillerClass)).thenReturn("InvalidFiller");
        when(renderingManager.getShortForm(axisClass)).thenReturn("Axis");
        when(renderingManager.getShortForm(rootClass)).thenReturn("GenericScaleTopClass");
        when(renderingManager.getShortForm(superclassClass)).thenReturn("Superclass");
        when(renderingManager.getShortForm(entityClass)).thenReturn("Entity");

        // Execute
        ValidateLogicalDefinitionsFromApiResult result = handler.execute(action, executionContext);

        // Verify
        assertThat(result.messages(), hasSize(2));
        assertThat(result.messages(), hasItem(containsString("Filler 'InvalidFiller' is not valid")));
        assertThat(result.messages(), hasItem(containsString("Logical definition superclass 'Superclass' is not an ancestor")));
    }

    @Test
    public void should_execute_giveErrorMessage_whenAxisNotInMapping() throws Exception {
        // Setup - axis not in mapping
        ValidateLogicalDefinitionsFromApiAction action = new ValidateLogicalDefinitionsFromApiAction(
                projectId,
                ENTITY_IRI,
                List.of(new ValidateLogicalDefinitionsFromApiAction.LogicalDefinition(
                        SUPERCLASS_IRI,
                        List.of(new ValidateLogicalDefinitionsFromApiAction.Relationship(
                                AXIS_IRI,
                                FILLER_IRI
                        ))
                )),
                List.of()
        );

        GetPostcoordinationAxisToGenericScaleResponse response = GetPostcoordinationAxisToGenericScaleResponse.create(
                List.of() // Empty - axis not in mapping
        );
        
        CompletableFuture<GetPostcoordinationAxisToGenericScaleResponse> future = 
                CompletableFuture.completedFuture(response);
        when(getPostcoordinationAxisToGenericScaleExecutor.execute(any(), any())).thenReturn(future);

        OWLClass entityClass = DataFactory.getOWLClass(IRI.create(ENTITY_IRI));
        OWLClass superclassClass = DataFactory.getOWLClass(IRI.create(SUPERCLASS_IRI));

        when(classHierarchyProvider.isAncestor(entityClass, superclassClass)).thenReturn(true);

        // Execute
        ValidateLogicalDefinitionsFromApiResult result = handler.execute(action, executionContext);

        // Verify - no error for filler validation (skipped), but superclass validation still runs
        // Verify
        assertThat(result.messages(), hasSize(1));
        assertThat(result.messages(), hasItem(containsString("Axis not available: ")));
    }

    @Test
    public void should_execute_skipValidation_whenHierarchyProviderNotAvailable() throws Exception {
        // Setup
        ValidateLogicalDefinitionsFromApiAction action = new ValidateLogicalDefinitionsFromApiAction(
                projectId,
                ENTITY_IRI,
                List.of(new ValidateLogicalDefinitionsFromApiAction.LogicalDefinition(
                        SUPERCLASS_IRI,
                        List.of(new ValidateLogicalDefinitionsFromApiAction.Relationship(
                                AXIS_IRI,
                                FILLER_IRI
                        ))
                )),
                List.of()
        );

        PostcoordinationAxisToGenericScale axisToScale = new PostcoordinationAxisToGenericScale(
                AXIS_IRI,
                GENERIC_SCALE_TOP_CLASS_IRI,
                "false"
        );
        GetPostcoordinationAxisToGenericScaleResponse response = GetPostcoordinationAxisToGenericScaleResponse.create(
                List.of(axisToScale)
        );
        
        CompletableFuture<GetPostcoordinationAxisToGenericScaleResponse> future = 
                CompletableFuture.completedFuture(response);
        when(getPostcoordinationAxisToGenericScaleExecutor.execute(any(), any())).thenReturn(future);

        OWLClass rootClass = DataFactory.getOWLClass(IRI.create(GENERIC_SCALE_TOP_CLASS_IRI));
        OWLClass entityClass = DataFactory.getOWLClass(IRI.create(ENTITY_IRI));
        OWLClass superclassClass = DataFactory.getOWLClass(IRI.create(SUPERCLASS_IRI));

        ClassHierarchyDescriptor hierarchyDescriptor = ClassHierarchyDescriptor.create(Set.of(rootClass));
        when(hierarchyProviderManager.getHierarchyProvider(hierarchyDescriptor))
                .thenReturn(Optional.empty()); // No hierarchy provider available
        when(classHierarchyProvider.isAncestor(entityClass, superclassClass)).thenReturn(true);

        // Execute
        ValidateLogicalDefinitionsFromApiResult result = handler.execute(action, executionContext);

        // Verify - no error for filler validation (skipped), but superclass validation still runs
        assertThat(result.messages(), is(empty()));
    }

    @Test
    public void should_execute_withError_whenInterruptedException() throws Exception {
        // Setup
        ValidateLogicalDefinitionsFromApiAction action = createValidAction();
        
        CompletableFuture<GetPostcoordinationAxisToGenericScaleResponse> future = new CompletableFuture<>();
        future.completeExceptionally(new InterruptedException("Interrupted"));
        when(getPostcoordinationAxisToGenericScaleExecutor.execute(any(), any())).thenReturn(future);

        // Execute
        ValidateLogicalDefinitionsFromApiResult result = handler.execute(action, executionContext);

        // Verify
        assertThat(result.messages(), hasSize(1));
        assertThat(result.messages().get(0), containsString("Error during validation"));
        assertThat(result.messages().get(0), containsString("Interrupted"));
    }

    @Test
    public void should_execute_withError_whenExecutionException() throws Exception {
        // Setup
        ValidateLogicalDefinitionsFromApiAction action = createValidAction();
        
        CompletableFuture<GetPostcoordinationAxisToGenericScaleResponse> future = new CompletableFuture<>();
        future.completeExceptionally(new ExecutionException(new RuntimeException("Execution failed")));
        when(getPostcoordinationAxisToGenericScaleExecutor.execute(any(), any())).thenReturn(future);

        // Execute
        ValidateLogicalDefinitionsFromApiResult result = handler.execute(action, executionContext);

        // Verify
        assertThat(result.messages(), hasSize(1));
        assertThat(result.messages().get(0), containsString("Error during validation"));
    }

    @Test
    public void should_execute_withError_whenTimeoutException() throws Exception {
        // Setup
        ValidateLogicalDefinitionsFromApiAction action = createValidAction();
        
        CompletableFuture<GetPostcoordinationAxisToGenericScaleResponse> future = new CompletableFuture<>();
        future.completeExceptionally(new TimeoutException("Timeout"));
        when(getPostcoordinationAxisToGenericScaleExecutor.execute(any(), any())).thenReturn(future);

        // Execute
        ValidateLogicalDefinitionsFromApiResult result = handler.execute(action, executionContext);

        // Verify
        assertThat(result.messages(), hasSize(1));
        assertThat(result.messages().get(0), containsString("Error during validation"));
        assertThat(result.messages().get(0), containsString("Timeout"));
    }

    @Test
    public void should_execute_handleExceptionInValidateFillerInHierarchy() throws Exception {
        // Setup - exception during hierarchy validation
        ValidateLogicalDefinitionsFromApiAction action = new ValidateLogicalDefinitionsFromApiAction(
                projectId,
                ENTITY_IRI,
                List.of(new ValidateLogicalDefinitionsFromApiAction.LogicalDefinition(
                        SUPERCLASS_IRI,
                        List.of(new ValidateLogicalDefinitionsFromApiAction.Relationship(
                                AXIS_IRI,
                                FILLER_IRI
                        ))
                )),
                List.of()
        );

        PostcoordinationAxisToGenericScale axisToScale = new PostcoordinationAxisToGenericScale(
                AXIS_IRI,
                GENERIC_SCALE_TOP_CLASS_IRI,
                "false"
        );
        GetPostcoordinationAxisToGenericScaleResponse response = GetPostcoordinationAxisToGenericScaleResponse.create(
                List.of(axisToScale)
        );
        
        CompletableFuture<GetPostcoordinationAxisToGenericScaleResponse> future = 
                CompletableFuture.completedFuture(response);
        when(getPostcoordinationAxisToGenericScaleExecutor.execute(any(), any())).thenReturn(future);

        OWLClass rootClass = DataFactory.getOWLClass(IRI.create(GENERIC_SCALE_TOP_CLASS_IRI));
        OWLClass entityClass = DataFactory.getOWLClass(IRI.create(ENTITY_IRI));
        OWLClass superclassClass = DataFactory.getOWLClass(IRI.create(SUPERCLASS_IRI));

        ClassHierarchyDescriptor hierarchyDescriptor = ClassHierarchyDescriptor.create(Set.of(rootClass));
        when(hierarchyProviderManager.getHierarchyProvider(hierarchyDescriptor))
                .thenThrow(new RuntimeException("Hierarchy error")); // Exception during hierarchy creation
        when(classHierarchyProvider.isAncestor(entityClass, superclassClass)).thenReturn(true);

        // Execute - should not throw, but skip the validation
        ValidateLogicalDefinitionsFromApiResult result = handler.execute(action, executionContext);

        // Verify - no error message added, validation skipped gracefully
        assertThat(result.messages(), is(empty()));
    }

    private ValidateLogicalDefinitionsFromApiAction createValidAction() {
        return new ValidateLogicalDefinitionsFromApiAction(
                projectId,
                ENTITY_IRI,
                List.of(new ValidateLogicalDefinitionsFromApiAction.LogicalDefinition(
                        SUPERCLASS_IRI,
                        List.of(new ValidateLogicalDefinitionsFromApiAction.Relationship(
                                AXIS_IRI,
                                FILLER_IRI
                        ))
                )),
                List.of()
        );
    }
}
