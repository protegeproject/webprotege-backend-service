package edu.stanford.protege.webprotege.permissions;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.dispatch.RequestContext;
import edu.stanford.protege.webprotege.dispatch.RequestValidator;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.user.UserId;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class GetProjectPermissionsActionHandler_TestCase {

    private GetProjectPermissionsActionHandler handler;

    @Mock
    private AccessManager accessManager;

    @Mock
    private UserId userId;

    @Mock
    private ProjectId projectId;

    @Mock
    private GetProjectPermissionsAction action;


    @Before
    public void setUp() throws Exception {
        handler = new GetProjectPermissionsActionHandler(accessManager);
    }

    @Test
    public void shouldReturnActionClass() {
        Class<GetProjectPermissionsAction> cls = handler.getActionClass();
        assertThat(cls, Matchers.<Class<GetProjectPermissionsAction>>is(GetProjectPermissionsAction.class));
    }

    @Test
    public void shouldAllowAnyOneToRetrievePermissions() {
        RequestContext requestContext = mock(RequestContext.class);
        RequestValidator validator = handler.getRequestValidator(action, requestContext);
        assertThat(validator.validateAction().isValid(), is(true));

    }
}
