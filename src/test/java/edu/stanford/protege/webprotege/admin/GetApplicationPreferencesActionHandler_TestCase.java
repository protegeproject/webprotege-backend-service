
package edu.stanford.protege.webprotege.admin;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.app.*;
import edu.stanford.protege.webprotege.authorization.ApplicationResource;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.dispatch.RequestContext;
import edu.stanford.protege.webprotege.dispatch.RequestValidationResult;
import edu.stanford.protege.webprotege.dispatch.RequestValidator;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static edu.stanford.protege.webprotege.access.BuiltInCapability.EDIT_APPLICATION_SETTINGS;
import static edu.stanford.protege.webprotege.authorization.Subject.forUser;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GetApplicationPreferencesActionHandler_TestCase {



    @Mock
    private AccessManager accessManager;

    @Mock
    private ApplicationSettingsManager applicationSettingsManager;

    @Mock
    private ApplicationSettings applicationSettings;

    @InjectMocks
    private GetApplicationSettingsActionHandler handler;

    private GetApplicationSettingsAction action = new GetApplicationSettingsAction();

    private UserId userId = edu.stanford.protege.webprotege.MockingUtils.mockUserId();

    private ExecutionContext executionContext = new ExecutionContext(userId, "DUMMY_JWT", "correlationId");

    private RequestContext requestContext = new RequestContext(userId, executionContext);

    public GetApplicationPreferencesActionHandler_TestCase() {
    }

    @BeforeEach
    public void setUp() throws Exception {
        when(applicationSettingsManager.getApplicationSettings()).thenReturn(applicationSettings);
    }

    @Test
    public void shouldCheckForPermission() {
        RequestValidator validator = handler.getRequestValidator(action, requestContext);
        RequestValidationResult result = validator.validateAction();
        assertThat(result.isInvalid(), is(true));
        verify(accessManager, times(1)).hasPermission(forUser(userId),
                                                      ApplicationResource.get(),
                                                      EDIT_APPLICATION_SETTINGS.getCapability(),executionContext);
    }

    @Test
    public void shouldGetApplicationSettings() {
        GetApplicationSettingsResult result = handler.execute(action, executionContext);
        verify(applicationSettingsManager, times(1)).getApplicationSettings();
        assertThat(result.settings(), is(applicationSettings));
    }

    @AfterEach
    public void tearDown(){
        Mockito.reset(accessManager, applicationSettingsManager, applicationSettings);
    }
}
