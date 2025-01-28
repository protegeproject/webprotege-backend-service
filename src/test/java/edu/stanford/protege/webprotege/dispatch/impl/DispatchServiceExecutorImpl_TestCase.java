package edu.stanford.protege.webprotege.dispatch.impl;

import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.dispatch.*;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.permissions.PermissionDeniedException;
import edu.stanford.protege.webprotege.project.ProjectManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20/02/15
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class DispatchServiceExecutorImpl_TestCase<A extends Action<R>, R extends Result> {

    @Mock
    private ApplicationActionHandlerRegistry registry;

    @Mock
    private ActionHandler<A, R> actionHandler;

    @Mock
    private A action;

    @Mock
    private RequestContext requestContext;

    private ExecutionContext executionContext = new ExecutionContext(new UserId("1"), "DUMMY_JWT");


    @Mock
    private RequestValidator requestValidator;

    private DispatchServiceExecutorImpl executor;

    @Mock
    private ProjectManager projectManager;


    @Mock
    private R result;

    @BeforeEach
    public void setUp() throws Exception {
        executor = new DispatchServiceExecutorImpl(registry, projectManager);
        when(registry.getActionHandler(action)).thenReturn(actionHandler);
        when(actionHandler.getRequestValidator(action, requestContext)).thenReturn(requestValidator);
        when(actionHandler.execute(action, executionContext)).thenReturn(result);
        when(requestValidator.validateAction()).thenReturn(RequestValidationResult.getValid());

    }

    @Test
public void shouldThrowActionExecutionException() {
    assertThrows(ActionExecutionException.class, () -> { 
        ExecutionContext executionContext = this.executionContext;
        when(actionHandler.execute(action, executionContext)).thenThrow(new RuntimeException("Exception as part of test"));
        executor.execute(action, requestContext, executionContext);
     });
}

    @Test
public void shouldThrowActionHandlerNotFoundException() {
    assertThrows(ActionHandlerNotFoundException.class, () -> { 
        when(registry.getActionHandler(action)).thenThrow(new ActionHandlerNotFoundException(action));
        executor.execute(action, requestContext, executionContext);
     });
}

    @Test
public void shouldThrowPermissionDeniedException() {
    assertThrows(PermissionDeniedException.class, () -> { 
        when(requestValidator.validateAction()).thenReturn(RequestValidationResult.getInvalid("Denied"));
        executor.execute(action, requestContext, executionContext);
     });
}

    @Test
    public void shouldExecuteAction() {
        executor.execute(action, requestContext, executionContext);
        verify(actionHandler, times(1)).execute(action, executionContext);
    }
}
