package edu.stanford.protege.webprotege.dispatch.impl;

import edu.stanford.protege.webprotege.MongoTestExtension;
import edu.stanford.protege.webprotege.RabbitTestExtension;
import edu.stanford.protege.webprotege.WebprotegeBackendMonolithApplication;
import edu.stanford.protege.webprotege.authorization.ActionId;
import edu.stanford.protege.webprotege.app.ApplicationSettings;
import edu.stanford.protege.webprotege.app.GetApplicationSettingsAction;
import edu.stanford.protege.webprotege.app.SetApplicationSettingsAction;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.user.GetUserIdCompletionsAction;
import edu.stanford.protege.webprotege.mail.GetEmailAddressAction;
import edu.stanford.protege.webprotege.mail.SetEmailAddressAction;
import edu.stanford.protege.webprotege.permissions.GetProjectPermissionsAction;
import edu.stanford.protege.webprotege.permissions.RebuildPermissionsAction;
import edu.stanford.protege.webprotege.perspective.GetPerspectivesAction;
import edu.stanford.protege.webprotege.project.*;
import edu.stanford.protege.webprotege.user.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;

@SpringBootTest(classes = WebprotegeBackendMonolithApplication.class)
@ExtendWith({RabbitTestExtension.class, MongoTestExtension.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ApplicationActionHandlerRegistry_Test {

    @Autowired
    private ApplicationActionHandlerRegistry registry;

    @BeforeEach
    public void setUp() throws Exception {
    }

    @Test
    public void shouldContainGetAvailableProjectsActions() {
        var handler = registry.getActionHandler(new GetAvailableProjectsAction());
        assertThat(handler, is(notNullValue()));
    }

    @Test
    public void shouldContainCreateNewProjectActionHandler() {
        var handler = registry.getActionHandler(CreateNewProjectAction.create(ProjectId.generate(),
                                                                              mock(NewProjectSettings.class)));
        assertThat(handler, is(notNullValue()));
    }

    @Test
    public void shouldContainGetApplicationSettingsActionHandler() {
        var handler = registry.getActionHandler(new GetApplicationSettingsAction());
        assertThat(handler, is(notNullValue()));
    }


    @Test
    public void shouldContainGetAvailableProjectsWithPermissionActionHandler() {
        var handler = registry.getActionHandler(GetAvailableProjectsWithPermissionAction.create(new ActionId("OtherAction")));
        assertThat(handler, is(notNullValue()));
    }

    @Test
    public void shouldContainGetEmailAddressActionHandler() {
        var handler = registry.getActionHandler(GetEmailAddressAction.create(edu.stanford.protege.webprotege.MockingUtils.mockUserId()));
        assertThat(handler, is(notNullValue()));
    }

    @Test
    public void shouldContainGetPerspectivesActionHandler() {
        var handler = registry.getActionHandler(new GetPerspectivesAction(ProjectId.generate(),
                                                                             edu.stanford.protege.webprotege.MockingUtils.mockUserId()));
        assertThat(handler, is(notNullValue()));
    }

//    @Test
//    public void shouldContainGetProjectDetailsActionHandler() {
//        var handler = registry.getActionHandler(GetProjectDetailsAction.create(ProjectId.generate()));
//        assertThat(handler, is(notNullValue()));
//    }

    @Test
    public void shouldContainGetProjectPermissionsActionHandler() {
        var handler = registry.getActionHandler(new GetProjectPermissionsAction(ProjectId.generate(),
                                                                                   edu.stanford.protege.webprotege.MockingUtils.mockUserId()));
        assertThat(handler, is(notNullValue()));
    }

    @Test
    public void shouldContainGetUserIdCompletionsActionHandler() {
        var handler = registry.getActionHandler(GetUserIdCompletionsAction.create("abc"));
        assertThat(handler, is(notNullValue()));
    }

//    @Test
//    public void shouldContainLoadProjectActionHandler() {
//        var handler = registry.getActionHandler(new LoadProjectAction(ProjectId.generate()));
//        assertThat(handler, is(notNullValue()));
//    }

    @Test
    public void shouldContainMoveProjectsToTrashActionHandler() {
        var handler = registry.getActionHandler(MoveProjectToTrashAction.create(ProjectId.generate()));
        assertThat(handler, is(notNullValue()));
    }

    @Test
    public void shouldContainRebuildPermissionsActionHandler() {
        var handler = registry.getActionHandler(new RebuildPermissionsAction());
        assertThat(handler, is(notNullValue()));
    }

    @Test
    public void shouldContainRemoveProjectsFromTrashActionHandler() {
        var handler = registry.getActionHandler(RemoveProjectFromTrashAction.create(ProjectId.generate()));
        assertThat(handler, is(notNullValue()));
    }

    @Test
    public void shouldContainSetApplicationSettingsActionHandler() {
        var handler = registry.getActionHandler(SetApplicationSettingsAction.create(mock(ApplicationSettings.class)));
        assertThat(handler, is(notNullValue()));
    }

    @Test
    public void shouldContainSetEmailAddressActionHandler() {
        var handler = registry.getActionHandler(new SetEmailAddressAction(edu.stanford.protege.webprotege.MockingUtils.mockUserId(),
                                                                             new EmailAddress("abc")));
        assertThat(handler, is(notNullValue()));
    }
}