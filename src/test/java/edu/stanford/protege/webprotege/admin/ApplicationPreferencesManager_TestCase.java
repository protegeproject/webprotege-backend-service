package edu.stanford.protege.webprotege.admin;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInCapability;
import edu.stanford.protege.webprotege.app.*;
import edu.stanford.protege.webprotege.authorization.ApplicationResource;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static edu.stanford.protege.webprotege.app.AccountCreationSetting.ACCOUNT_CREATION_ALLOWED;
import static edu.stanford.protege.webprotege.app.AccountCreationSetting.ACCOUNT_CREATION_NOT_ALLOWED;
import static edu.stanford.protege.webprotege.app.ProjectCreationSetting.EMPTY_PROJECT_CREATION_ALLOWED;
import static edu.stanford.protege.webprotege.app.ProjectCreationSetting.EMPTY_PROJECT_CREATION_NOT_ALLOWED;
import static edu.stanford.protege.webprotege.app.ProjectUploadSetting.PROJECT_UPLOAD_ALLOWED;
import static edu.stanford.protege.webprotege.app.ProjectUploadSetting.PROJECT_UPLOAD_NOT_ALLOWED;
import static edu.stanford.protege.webprotege.authorization.Subject.forAnySignedInUser;
import static edu.stanford.protege.webprotege.authorization.Subject.forGuestUser;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Mar 2017
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ApplicationPreferencesManager_TestCase {

    public static final String THE_APP_NAME = "TheAppName";

    public static final String THE_SYSTEM_NOTIFICATION_EMAIL_ADDRESS = "TheSystemNotificationEmailAddress";

    private ApplicationSettingsManager manager;

    @Mock
    private AccessManager accessManager;

    @Mock
    private ApplicationPreferencesStore applicationPreferencesStore;

    @Mock
    private ApplicationPreferences applicationPreferences;

    @Mock
    private ApplicationLocation applicationLocation;

    @BeforeEach
    public void setUp() throws Exception {
        manager = new ApplicationSettingsManager(accessManager,
                                                 applicationPreferencesStore);

        when(applicationPreferences.getApplicationName()).thenReturn(THE_APP_NAME);
        when(applicationPreferences.getSystemNotificationEmailAddress()).thenReturn(THE_SYSTEM_NOTIFICATION_EMAIL_ADDRESS);
        when(applicationPreferences.getApplicationLocation()).thenReturn(applicationLocation);
        when(applicationPreferencesStore.getApplicationPreferences()).thenReturn(applicationPreferences);
    }

    @Test
    public void shouldGetApplicationSettings() {
        ApplicationSettings applicationSettings = manager.getApplicationSettings(new ExecutionContext());
        assertThat(applicationSettings.getApplicationName(), is(THE_APP_NAME));
        assertThat(applicationSettings.getSystemNotificationEmailAddress().getEmailAddress(), is(THE_SYSTEM_NOTIFICATION_EMAIL_ADDRESS));
        assertThat(applicationSettings.getApplicationLocation(), is(applicationLocation));
    }

    @Test
    public void shouldGetAccountCreationNotAllowed() {
        ApplicationSettings applicationSettings = manager.getApplicationSettings(new ExecutionContext());
        assertThat(applicationSettings.getAccountCreationSetting(), is(ACCOUNT_CREATION_NOT_ALLOWED));
    }

    @Test
    public void shouldGetAccountCreationAllowed() {
        when(accessManager.hasPermission(forGuestUser(),
                                         ApplicationResource.get(),
                                         BuiltInCapability.CREATE_ACCOUNT)).thenReturn(true);
        ApplicationSettings applicationSettings = manager.getApplicationSettings(new ExecutionContext());
        assertThat(applicationSettings.getAccountCreationSetting(), is(ACCOUNT_CREATION_ALLOWED));
    }

    @Test
    public void shouldGetProjectCreationNotAllowed() {
        ApplicationSettings applicationSettings = manager.getApplicationSettings(new ExecutionContext());
        assertThat(applicationSettings.getProjectCreationSetting(), is(EMPTY_PROJECT_CREATION_NOT_ALLOWED));
    }

    @Test
    public void shouldGetProjectCreationAllowed() {
        when(accessManager.hasPermission(forAnySignedInUser(),
                                         ApplicationResource.get(),
                                         BuiltInCapability.CREATE_EMPTY_PROJECT)).thenReturn(true);
        ApplicationSettings applicationSettings = manager.getApplicationSettings(new ExecutionContext());
        assertThat(applicationSettings.getProjectCreationSetting(), is(EMPTY_PROJECT_CREATION_ALLOWED));
    }

    @Test
    public void shouldGetProjectUploadNotAllowed() {
        ApplicationSettings applicationSettings = manager.getApplicationSettings(new ExecutionContext());
        assertThat(applicationSettings.getProjectUploadSetting(), is(PROJECT_UPLOAD_NOT_ALLOWED));
    }

    @Test
    public void shouldGetProjectUploadAllowed() {
        when(accessManager.hasPermission(forAnySignedInUser(),
                                         ApplicationResource.get(),
                                         BuiltInCapability.UPLOAD_PROJECT)).thenReturn(true);
        ApplicationSettings applicationSettings = manager.getApplicationSettings(new ExecutionContext());
        assertThat(applicationSettings.getProjectUploadSetting(), is(PROJECT_UPLOAD_ALLOWED));
    }
}
