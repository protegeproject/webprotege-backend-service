package edu.stanford.protege.webprotege.projectsettings;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.project.ProjectDetailsManager;
import edu.stanford.protege.webprotege.project.ProjectManager;
import edu.stanford.protege.webprotege.project.ProjectDetails;
import edu.stanford.protege.webprotege.project.ProjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25/11/14
 */
@RunWith(MockitoJUnitRunner.class)
public class SetProjectSettingsActionHandler_TestCase {

    @Mock
    private SetProjectSettingsAction action;

    @Mock
    private ProjectSettings projectSettings;

    @Mock
    private ProjectId projectId;

    private SetProjectSettingsActionHandler handler;

    @Mock
    private ProjectManager projectManager;

    @Mock
    private AccessManager accessManager;

    @Mock
    private ProjectDetailsManager detailsManager;

    @Mock
    private ProjectDetails projectDetails;

    @Before
    public void setUp() throws Exception {
        when(action.getProjectId()).thenReturn(projectId);
        when(action.getProjectSettings()).thenReturn(projectSettings);
        when(detailsManager.getProjectSettings(projectId)).thenReturn(projectSettings);
        handler = new SetProjectSettingsActionHandler(accessManager, detailsManager);
    }

    @Test
    public void shouldSetProjectSettings() {
        handler.execute(action, mock(ExecutionContext.class));
        verify(detailsManager, times(1)).setProjectSettings(projectSettings);
    }
}
