package edu.stanford.protege.webprotege.dispatch;

import edu.stanford.protege.webprotege.permissions.PermissionDeniedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28 Oct 2018
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ActionExecutionResult_TestCase {

    @Mock
    private DispatchServiceResultContainer resultContainer;

    @Mock
    private PermissionDeniedException permissionDeniedException;

    @Mock
    private ActionExecutionException actionExecutionException;

    @Test
    public void shouldCreateResultFromResult() {
        ActionExecutionResult result = ActionExecutionResult.get(resultContainer);
        assertThat(result.getResult(), is(Optional.of(resultContainer)));
        assertThat(result.getActionExecutionException().isPresent(), is(false));
        assertThat(result.getPermissionDeniedException().isPresent(), is(false));
    }

    @Test
    public void shouldCreateResultFromPermissionDeniedException() {
        ActionExecutionResult result = ActionExecutionResult.get(permissionDeniedException);
        assertThat(result.getPermissionDeniedException(), is(Optional.of(permissionDeniedException)));
        assertThat(result.getResult().isPresent(), is(false));
        assertThat(result.getActionExecutionException().isPresent(), is(false));
    }

    @Test
    public void shouldCreateResultFromActionExecutionException() {
        ActionExecutionResult result = ActionExecutionResult.get(actionExecutionException);
        assertThat(result.getActionExecutionException(), is(Optional.of(actionExecutionException)));
        assertThat(result.getResult().isPresent(), is(false));
        assertThat(result.getPermissionDeniedException().isPresent(), is(false));
    }
}
