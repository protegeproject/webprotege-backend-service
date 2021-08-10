package edu.stanford.protege.webprotege.dispatch.impl;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.authorization.api.ActionId;
import edu.stanford.protege.webprotege.app.ApplicationSettings;
import edu.stanford.protege.webprotege.app.GetApplicationSettingsAction;
import edu.stanford.protege.webprotege.app.SetApplicationSettingsAction;
import edu.stanford.protege.webprotege.auth.ChangePasswordAction;
import edu.stanford.protege.webprotege.auth.Password;
import edu.stanford.protege.webprotege.auth.PerformLoginAction;
import edu.stanford.protege.webprotege.chgpwd.ResetPasswordAction;
import edu.stanford.protege.webprotege.chgpwd.ResetPasswordData;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.event.EventTag;
import edu.stanford.protege.webprotege.event.GetProjectEventsAction;
import edu.stanford.protege.webprotege.itemlist.GetPersonIdCompletionsAction;
import edu.stanford.protege.webprotege.itemlist.GetPersonIdItemsAction;
import edu.stanford.protege.webprotege.itemlist.GetUserIdCompletionsAction;
import edu.stanford.protege.webprotege.mail.GetEmailAddressAction;
import edu.stanford.protege.webprotege.mail.SetEmailAddressAction;
import edu.stanford.protege.webprotege.permissions.GetProjectPermissionsAction;
import edu.stanford.protege.webprotege.permissions.RebuildPermissionsAction;
import edu.stanford.protege.webprotege.perspective.GetPerspectivesAction;
import edu.stanford.protege.webprotege.project.*;
import edu.stanford.protege.webprotege.user.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ApplicationActionHandlerRegistry_Test {

    @Autowired
    private ApplicationActionHandlerRegistry registry;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void shouldContainGetAvailableProjectsActions() {
        var handler = registry.getActionHandler(GetAvailableProjectsAction.create());
        assertThat(handler, is(notNullValue()));
    }

    @Test
    public void shouldContainChangePasswordActionHandler() {
        var handler = registry.getActionHandler(ChangePasswordAction.create(edu.stanford.protege.webprotege.MockingUtils.mockUserId(),
                                                                            mock(Password.class),
                                                                            mock(Password.class)));
        assertThat(handler, is(notNullValue()));
    }

    @Test
    public void shouldContainCreateNewProjectActionHandler() {
        var handler = registry.getActionHandler(CreateNewProjectAction.create(mock(NewProjectSettings.class)));
        assertThat(handler, is(notNullValue()));
    }

    @Test
    public void shouldContainCreateUserAccountActionHandler() {
        var handler = registry.getActionHandler(CreateUserAccountAction.create(edu.stanford.protege.webprotege.MockingUtils.mockUserId(),
                                                                                    mock(EmailAddress.class),
                                                                                    mock(Password.class)));
        assertThat(handler, is(notNullValue()));
    }

    @Test
    public void shouldContainGetApplicationSettingsActionHandler() {
        var handler = registry.getActionHandler(GetApplicationSettingsAction.create());
        assertThat(handler, is(notNullValue()));
    }


    @Test
    public void shouldContainGetAvailableProjectsWithPermissionActionHandler() {
        var handler = registry.getActionHandler(GetAvailableProjectsWithPermissionAction.create(mock(ActionId.class)));
        assertThat(handler, is(notNullValue()));
    }
    @Test
    public void shouldContainGetCurrentUserInSessionActionHandler() {
        var handler = registry.getActionHandler(GetCurrentUserInSessionAction.create());
        assertThat(handler, is(notNullValue()));
    }

    @Test
    public void shouldContainGetEmailAddressActionHandler() {
        var handler = registry.getActionHandler(GetEmailAddressAction.create(edu.stanford.protege.webprotege.MockingUtils.mockUserId()));
        assertThat(handler, is(notNullValue()));
    }

    @Test
    public void shouldContainGetPersonIdCompletionsActionHandler() {
        var handler = registry.getActionHandler(GetPersonIdCompletionsAction.create("abc"));
        assertThat(handler, is(notNullValue()));
    }

    @Test
    public void shouldContainGetPersonIdItemsActionHandler() {
        var handler = registry.getActionHandler(GetPersonIdItemsAction.create(ImmutableList.of("A")));
        assertThat(handler, is(notNullValue()));
    }

    @Test
    public void shouldContainGetPerspectivesActionHandler() {
        var handler = registry.getActionHandler(GetPerspectivesAction.create(ProjectId.generate(),
                                                                             edu.stanford.protege.webprotege.MockingUtils.mockUserId()));
        assertThat(handler, is(notNullValue()));
    }

    @Test
    public void shouldContainGetProjectDetailsActionHandler() {
        var handler = registry.getActionHandler(GetProjectDetailsAction.create(ProjectId.generate()));
        assertThat(handler, is(notNullValue()));
    }

    @Test
    public void shouldContainGetProjectEventsActionHandler() {
        var handler = registry.getActionHandler(GetProjectEventsAction.create(mock(EventTag.class),
                                                                              ProjectId.generate()));
        assertThat(handler, is(notNullValue()));
    }

    @Test
    public void shouldContainGetProjectPermissionsActionHandler() {
        var handler = registry.getActionHandler(GetProjectPermissionsAction.create(ProjectId.generate(),
                                                                                   edu.stanford.protege.webprotege.MockingUtils.mockUserId()));
        assertThat(handler, is(notNullValue()));
    }

    @Test
    public void shouldContainGetUserIdCompletionsActionHandler() {
        var handler = registry.getActionHandler(GetUserIdCompletionsAction.create("abc"));
        assertThat(handler, is(notNullValue()));
    }

    @Test
    public void shouldContainLoadProjectActionHandler() {
        var handler = registry.getActionHandler(LoadProjectAction.create(ProjectId.generate()));
        assertThat(handler, is(notNullValue()));
    }

    @Test
    public void shouldContainLogOutUserActionHandler() {
        var handler = registry.getActionHandler(LogOutUserAction.create());
        assertThat(handler, is(notNullValue()));
    }

    @Test
    public void shouldContainMoveProjectsToTrashActionHandler() {
        var handler = registry.getActionHandler(MoveProjectsToTrashAction.create(ProjectId.generate()));
        assertThat(handler, is(notNullValue()));
    }

    @Test
    public void shouldContainPerformLoginActionHandler() {
        var handler = registry.getActionHandler(PerformLoginAction.create(edu.stanford.protege.webprotege.MockingUtils.mockUserId(),
                                                                          mock(Password.class)));
        assertThat(handler, is(notNullValue()));
    }

    @Test
    public void shouldContainRebuildPermissionsActionHandler() {
        var handler = registry.getActionHandler(RebuildPermissionsAction.create());
        assertThat(handler, is(notNullValue()));
    }

    @Test
    public void shouldContainRemoveProjectsFromTrashActionHandler() {
        var handler = registry.getActionHandler(RemoveProjectFromTrashAction.create(ProjectId.generate()));
        assertThat(handler, is(notNullValue()));
    }

    @Test
    public void shouldContainResetPasswordActionHandler() {
        var handler = registry.getActionHandler(ResetPasswordAction.create(mock(ResetPasswordData.class)));
        assertThat(handler, is(notNullValue()));
    }

    @Test
    public void shouldContainSetApplicationSettingsActionHandler() {
        var handler = registry.getActionHandler(SetApplicationSettingsAction.create(mock(ApplicationSettings.class)));
        assertThat(handler, is(notNullValue()));
    }

    @Test
    public void shouldContainSetEmailAddressActionHandler() {
        var handler = registry.getActionHandler(SetEmailAddressAction.create(edu.stanford.protege.webprotege.MockingUtils.mockUserId(),
                                                                             "abc"));
        assertThat(handler, is(notNullValue()));
    }
}