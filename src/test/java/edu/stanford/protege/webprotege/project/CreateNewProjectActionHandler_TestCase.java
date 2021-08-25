package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.app.UserInSessionFactory;
import edu.stanford.protege.webprotege.authorization.Resource;
import edu.stanford.protege.webprotege.authorization.Subject;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.dispatch.RequestContext;
import edu.stanford.protege.webprotege.dispatch.RequestValidationResult;
import edu.stanford.protege.webprotege.dispatch.RequestValidator;
import edu.stanford.protege.webprotege.sharing.ProjectSharingSettingsManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.app.UserInSession;
import edu.stanford.protege.webprotege.lang.DefaultDisplayNameSettingsFactory;
import edu.stanford.protege.webprotege.permissions.PermissionDeniedException;
import edu.stanford.protege.webprotege.common.UserId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class CreateNewProjectActionHandler_TestCase {

    private CreateNewProjectActionHandler handler;

    @Mock
    private ProjectManager projectManager;

    @Mock
    private ProjectDetailsManager projectDetailsManager;

    @Mock
    private ProjectSharingSettingsManager projectSharingSettingsManager;

    private NewProjectSettings newProjectSettings;

    @Mock
    private ExecutionContext executionContext;

    private UserId userId = edu.stanford.protege.webprotege.MockingUtils.mockUserId();

    private ProjectId projectId = ProjectId.generate();

    @Mock
    private ProjectDetails projectDetails;

    @Mock
    private RequestContext requestContext;

    @Mock
    private AccessManager accessManager;

    @Mock
    private UserInSessionFactory userInSessionFactory;

    @Mock
    private UserInSession userInSession;

    private DefaultDisplayNameSettingsFactory displayNameSettingsFactory;

    private String langTag = "en-GB";

    @Before
    public void setUp() throws Exception {
        displayNameSettingsFactory = new DefaultDisplayNameSettingsFactory();
        newProjectSettings = NewProjectSettings.get(UserId.valueOf("The Owner"),
                                                    "The display name",
                                                    langTag,
                                                    "The Project Description");
        handler = new CreateNewProjectActionHandler(projectManager,
                                                    projectDetailsManager,
                                                    accessManager,
                                                    userInSessionFactory);
        when(projectManager.createNewProject(this.newProjectSettings)).thenReturn(projectId);
        when(executionContext.getUserId()).thenReturn(userId);
        when(userInSessionFactory.getUserInSession(any())).thenReturn(userInSession);
        when(projectDetailsManager.getProjectDetails(projectId)).thenReturn(projectDetails);
        when(requestContext.getUserId()).thenReturn(userId);
        setPermission(true);

    }

    void setPermission(boolean allowed) {
        when(accessManager.hasPermission(
                Mockito.any(Subject.class),
                Mockito.any(Resource.class),
                Mockito.any(BuiltInAction.class))).thenReturn(allowed);
    }

    private void executeCreateNewProject() {
        handler.execute(new CreateNewProjectAction(newProjectSettings), executionContext);
    }

    @Test
    public void shouldCreateNewProject() throws Exception {
        setPermission(true);
        executeCreateNewProject();
        verify(projectManager, times(1)).createNewProject(newProjectSettings);
    }

    @Test(expected = PermissionDeniedException.class)
    public void shouldDenyCreateNewProject() throws Exception {
        setPermission(false);
        executeCreateNewProject();
    }

    @Test
    public void shouldRegisterNewProject() {
        executeCreateNewProject();
        verify(projectDetailsManager, times(1)).registerProject(projectId, newProjectSettings);
    }

    @Test(expected = PermissionDeniedException.class)
    public void shouldDenyRegisterNewProject() {
        setPermission(false);
        executeCreateNewProject();
    }


    @Test
    public void shouldNotAllowGuestsToCreateProjects() {
        userId = UserId.getGuest();
        CreateNewProjectAction action = new CreateNewProjectAction(newProjectSettings);
        RequestValidator validator = handler.getRequestValidator(action, requestContext);
        RequestValidationResult validationResult = validator.validateAction();
        assertThat(validationResult.isValid(), is(false));
    }
}
