package edu.stanford.protege.webprotege.dispatch.impl;

import edu.stanford.protege.webprotege.dispatch.*;
import edu.stanford.protege.webprotege.project.ProjectManager;
import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.dispatch.ActionExecutionException;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.permissions.PermissionDeniedException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class DispatchServiceExecutorImpl_TestCase<A extends Action<R>, R extends Result> {

    @Mock
    private ApplicationActionHandlerRegistry registry;

    @Mock
    private ActionHandler<A, R> actionHandler;

    @Mock
    private A action;

    @Mock
    private RequestContext requestContext;

    @Mock
    private ExecutionContext executionContext;

    @Mock
    private RequestValidator requestValidator;

    private DispatchServiceExecutorImpl executor;

    @Mock
    private ProjectManager projectManager;


    @Mock
    private R result;

    @Before
    public void setUp() throws Exception {
        executor = new DispatchServiceExecutorImpl(registry, projectManager);
        when(registry.getActionHandler(action)).thenReturn(actionHandler);
        when(actionHandler.getRequestValidator(action, requestContext)).thenReturn(requestValidator);
        when(actionHandler.execute(action, executionContext)).thenReturn(result);
        when(requestValidator.validateAction()).thenReturn(RequestValidationResult.getValid());

    }

    @Test(expected = ActionExecutionException.class)
    public void shouldThrowActionExecutionException() {
        ExecutionContext executionContext = this.executionContext;
        when(actionHandler.execute(action, executionContext)).thenThrow(new RuntimeException("Exception as part of test"));
        executor.execute(action, requestContext, executionContext);
    }

    @Test(expected = ActionHandlerNotFoundException.class)
    public void shouldThrowActionHandlerNotFoundException() {
        when(registry.getActionHandler(action)).thenThrow(new ActionHandlerNotFoundException(action));
        executor.execute(action, requestContext, executionContext);
    }

    @Test(expected = PermissionDeniedException.class)
    public void shouldThrowPermissionDeniedException() {
        when(requestValidator.validateAction()).thenReturn(RequestValidationResult.getInvalid("Denied"));
        executor.execute(action, requestContext, executionContext);
    }

    @Test
    public void shouldExecuteAction() {
        executor.execute(action, requestContext, executionContext);
        verify(actionHandler, times(1)).execute(action, executionContext);
    }
}
