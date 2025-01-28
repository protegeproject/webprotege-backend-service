package edu.stanford.protege.webprotege.projectsettings;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.project.ProjectDetailsManager;
import edu.stanford.protege.webprotege.project.ProjectManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25/11/14
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GetProjectSettingsActionHandler_TestCase {


    private GetProjectSettingsActionHandler actionHandler;


    private ProjectId projectId = ProjectId.generate();

    @Mock
    private ProjectSettings projectSettings;

    @Mock
    private ProjectDetailsManager mdm;

    @Mock
    private GetProjectSettingsAction action;

    private ExecutionContext executionContext = new ExecutionContext(new UserId("1"), "DUMMY_JWT");


    @Mock
    private ProjectManager projectManager;

    @Mock
    private AccessManager accessManager;

    @BeforeEach
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
