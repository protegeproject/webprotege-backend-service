
package edu.stanford.protege.webprotege.admin;

import edu.stanford.protege.webprotege.WebprotegeBackendMonolithApplication;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.app.ApplicationSettingsManager;
import edu.stanford.protege.webprotege.app.GetApplicationSettingsActionHandler;
import edu.stanford.protege.webprotege.authorization.ApplicationResource;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.dispatch.RequestContext;
import edu.stanford.protege.webprotege.dispatch.RequestValidationResult;
import edu.stanford.protege.webprotege.dispatch.RequestValidator;
import edu.stanford.protege.webprotege.app.ApplicationSettings;
import edu.stanford.protege.webprotege.app.GetApplicationSettingsAction;
import edu.stanford.protege.webprotege.app.GetApplicationSettingsResult;
import edu.stanford.protege.webprotege.common.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static edu.stanford.protege.webprotege.access.BuiltInAction.EDIT_APPLICATION_SETTINGS;
import static edu.stanford.protege.webprotege.authorization.Subject.forUser;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = WebprotegeBackendMonolithApplication.class)
public class GetApplicationPreferencesActionHandler_TestCase {

    private GetApplicationSettingsActionHandler handler;

    @Mock
    private AccessManager accessManager;

    @Mock
    private ApplicationSettingsManager applicationSettingsManager;

    private GetApplicationSettingsAction action = new GetApplicationSettingsAction();

    @Mock
    private ExecutionContext executionContext;

    private UserId userId = edu.stanford.protege.webprotege.MockingUtils.mockUserId();

    @Mock
    private RequestValidator requestValidator;

    @Mock
    private RequestContext requestContext;

    @Mock
    private ApplicationSettings applicationSettings;

    public GetApplicationPreferencesActionHandler_TestCase() {
    }

    @BeforeEach
    public void setUp() throws Exception {
        handler = new GetApplicationSettingsActionHandler(accessManager, applicationSettingsManager);
        when(requestContext.getUserId()).thenReturn(userId);
        when(applicationSettingsManager.getApplicationSettings()).thenReturn(applicationSettings);
    }

    @Test
    public void shouldCheckForPermission() {
        RequestValidator validator = handler.getRequestValidator(action, requestContext);
        RequestValidationResult result = validator.validateAction();
        assertThat(result.isInvalid(), is(true));
        verify(accessManager, times(1)).hasPermission(forUser(userId),
                                                      ApplicationResource.get(),
                                                      EDIT_APPLICATION_SETTINGS.getActionId());
    }

    @Test
    public void shouldGetAdminSettings() {
        GetApplicationSettingsResult result = handler.execute(action, executionContext);
        verify(applicationSettingsManager, times(1)).getApplicationSettings();
        assertThat(result.settings(), is(applicationSettings));
    }
}
