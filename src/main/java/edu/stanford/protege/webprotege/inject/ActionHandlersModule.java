package edu.stanford.protege.webprotege.inject;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import edu.stanford.protege.webprotege.app.GetApplicationSettingsActionHandler;
import edu.stanford.protege.webprotege.app.SetApplicationSettingsActionHandler;
import edu.stanford.protege.webprotege.auth.ChangePasswordActionHandler;
import edu.stanford.protege.webprotege.auth.PerformLoginActionHandler;
import edu.stanford.protege.webprotege.chgpwd.ResetPasswordActionHandler;
import edu.stanford.protege.webprotege.csv.GetCSVGridActionHandler;
import edu.stanford.protege.webprotege.dispatch.ApplicationActionHandler;
import edu.stanford.protege.webprotege.dispatch.handlers.*;
import edu.stanford.protege.webprotege.events.GetProjectEventsActionHandler;
import edu.stanford.protege.webprotege.itemlist.GetPersonIdCompletionsActionHandler;
import edu.stanford.protege.webprotege.itemlist.GetPersonIdItemsActionHandler;
import edu.stanford.protege.webprotege.itemlist.GetUserIdCompletionsActionHandler;
import edu.stanford.protege.webprotege.mail.GetEmailAddressActionHandler;
import edu.stanford.protege.webprotege.mail.SetEmailAddressActionHandler;
import edu.stanford.protege.webprotege.permissions.GetProjectPermissionsActionHandler;
import edu.stanford.protege.webprotege.permissions.RebuildPermissionsActionHandler;
import edu.stanford.protege.webprotege.perspective.GetPerspectivesActionHandler;
import edu.stanford.protege.webprotege.project.CreateNewProjectActionHandler;
import edu.stanford.protege.webprotege.project.GetAvailableProjectsWithPermissionActionHandler;
import edu.stanford.protege.webprotege.project.GetProjectDetailsActionHandler;
import edu.stanford.protege.webprotege.user.CreateUserAccountActionHandler;
import edu.stanford.protege.webprotege.user.LogOutUserActionHandler;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
@Module
public class ActionHandlersModule {

    @Provides @IntoSet
    public ApplicationActionHandler provideGetAvailableProjectsHandler(GetAvailableProjectsHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ApplicationActionHandler provideGetProjectDetailsActionHandler(GetProjectDetailsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ApplicationActionHandler provideLoadProjectActionHandler(LoadProjectActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ApplicationActionHandler provideCreateNewProjectActionHandler(CreateNewProjectActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ApplicationActionHandler provideGetProjectEventsActionHandler(GetProjectEventsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ApplicationActionHandler provideGetCurrentUserInSessionActionHandler(
            GetCurrentUserInSessionActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ApplicationActionHandler provideSetEmailAddressActionHandler(SetEmailAddressActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ApplicationActionHandler provideGetEmailAddressActionHandler(GetEmailAddressActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ApplicationActionHandler provideMoveProjectsToTrashActionHandler(MoveProjectsToTrashActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ApplicationActionHandler provideRemoveProjectsFromTrashActionHandler(
            RemoveProjectsFromTrashActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ApplicationActionHandler provideGetCSVGridActionHandler(GetCSVGridActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ApplicationActionHandler provideResetPasswordActionHandler(ResetPasswordActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ApplicationActionHandler provideLogOutUserActionHandler(LogOutUserActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ApplicationActionHandler providePerformLoginActionHandler(PerformLoginActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ApplicationActionHandler provideChangePasswordActionHandler(ChangePasswordActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ApplicationActionHandler provideCreateUserAccountActionHandler(CreateUserAccountActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ApplicationActionHandler provideGetPermissionsActionHandler(GetProjectPermissionsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ApplicationActionHandler provideGetPersonIdCompletionsActionHandler(
            GetPersonIdCompletionsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ApplicationActionHandler provideGetUserIdCompletionsActionHandler(GetUserIdCompletionsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ApplicationActionHandler provideGetPersonIdItemsActionHandler(GetPersonIdItemsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ApplicationActionHandler provideGetSystemSettingsActionHandler(GetApplicationSettingsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ApplicationActionHandler provideSetAdminSettingsActionHandler(SetApplicationSettingsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ApplicationActionHandler providesRebuildPermissionsActionHandler(RebuildPermissionsActionHandler handler) {
        return handler;
    }

    @Provides @IntoSet
    public ApplicationActionHandler providesGetAvailableProjectsWithPermissionActionHandler(GetAvailableProjectsWithPermissionActionHandler handler) {
        return handler;
    }


    @Provides @IntoSet
    public ApplicationActionHandler provideGetPerspectivesActionHandler(GetPerspectivesActionHandler handler) {
        return handler;
    }
}
