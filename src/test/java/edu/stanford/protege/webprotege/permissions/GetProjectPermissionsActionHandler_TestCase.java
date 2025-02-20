package edu.stanford.protege.webprotege.permissions;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.dispatch.RequestContext;
import edu.stanford.protege.webprotege.dispatch.RequestValidator;
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
import static org.mockito.Mockito.mock;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23/02/15
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GetProjectPermissionsActionHandler_TestCase {

    private GetProjectPermissionsActionHandler handler;

    @Mock
    private AccessManager accessManager;

    private UserId userId = edu.stanford.protege.webprotege.MockingUtils.mockUserId();

    private ProjectId projectId = ProjectId.generate();

    private GetProjectPermissionsAction action = new GetProjectPermissionsAction(ProjectId.generate(),
                                                                                 UserId.getGuest());


    @BeforeEach
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
