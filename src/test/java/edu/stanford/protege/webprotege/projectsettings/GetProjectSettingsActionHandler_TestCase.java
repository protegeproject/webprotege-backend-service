package edu.stanford.protege.webprotege.projectsettings;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.project.ProjectDetailsManager;
import edu.stanford.protege.webprotege.project.ProjectManager;
import edu.stanford.protege.webprotege.project.ProjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25/11/14
 */
@RunWith(MockitoJUnitRunner.class)
public class GetProjectSettingsActionHandler_TestCase {


    private GetProjectSettingsActionHandler actionHandler;


    @Mock
    private ProjectId projectId;

    @Mock
    private ProjectSettings projectSettings;

    @Mock
    private ProjectDetailsManager mdm;

    @Mock
    private GetProjectSettingsAction action;

    @Mock
    private ExecutionContext executionContext;

    @Mock
    private ProjectManager projectManager;

    @Mock
    private AccessManager accessManager;

    @Before
    public void setUp() throws Exception {
        actionHandler = new GetProjectSettingsActionHandler(accessManager, projectId, mdm);
        when(mdm.getProjectSettings(projectId)).thenReturn(projectSettings);
    }

    @Test
    public void shouldReturnSettings() {
        GetProjectSettingsResult result = actionHandler.execute(action, executionContext);
        assertThat(result.getSettings(), is(projectSettings));
    }
}
