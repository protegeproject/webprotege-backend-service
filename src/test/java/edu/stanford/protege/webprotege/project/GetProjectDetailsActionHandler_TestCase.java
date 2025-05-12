
package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.dispatch.RequestContext;
import edu.stanford.protege.webprotege.dispatch.validators.NullValidator;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GetProjectDetailsActionHandler_TestCase {

    private GetProjectDetailsActionHandler handler;

    @Mock
    private ProjectDetailsManager projectDetailsManager;

    @Mock
    private GetProjectDetailsAction action;

    @Mock
    private GetProjectDetailsResult result;

    private ProjectId projectId = ProjectId.generate();

    @Mock
    private ProjectDetails projectDetails;

    private ExecutionContext executionContext = new ExecutionContext(new UserId("1"), "DUMMY_JWT", "correlationId");


    @BeforeEach
    public void setUp() {
        handler = new GetProjectDetailsActionHandler(projectDetailsManager);
        when(action.projectId()).thenReturn(projectId);
        when(projectDetailsManager.getProjectDetails(projectId)).thenReturn(projectDetails);
    }

    @Test
public void shouldThrowNullPointerExceptionIf_projectDetailsManager_IsNull() {
    assertThrows(NullPointerException.class, () -> { 
        new GetProjectDetailsActionHandler(null);
     });
}

    @Test
    public void shouldImplementToString() {
        assertThat(handler.toString(), Matchers.startsWith("GetProjectDetailsActionHandler"));
    }

    @Test
    public void should_getRequestValidator() {
        assertThat(handler.getRequestValidator(action, mock(RequestContext.class)), is(NullValidator.get()));
    }

    @Test
    public void should_execute() {
        GetProjectDetailsResult res = handler.execute(action, executionContext);
        verify(projectDetailsManager).getProjectDetails(projectId);
        assertThat(res.getProjectDetails(), is(projectDetails));
    }

}
