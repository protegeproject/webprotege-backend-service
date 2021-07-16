package edu.stanford.protege.webprotege;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.app.ApplicationSettingsManager;
import edu.stanford.protege.webprotege.app.GetApplicationSettingsActionHandler;
import edu.stanford.protege.webprotege.app.SetApplicationSettingsActionHandler;
import edu.stanford.protege.webprotege.app.UserInSessionFactory;
import edu.stanford.protege.webprotege.auth.AuthenticationManager;
import edu.stanford.protege.webprotege.auth.ChangePasswordActionHandler;
import edu.stanford.protege.webprotege.auth.PerformLoginActionHandler;
import edu.stanford.protege.webprotege.chgpwd.ResetPasswordActionHandler;
import edu.stanford.protege.webprotege.chgpwd.ResetPasswordMailer;
import edu.stanford.protege.webprotege.dispatch.ApplicationActionHandler;
import edu.stanford.protege.webprotege.dispatch.handlers.*;
import edu.stanford.protege.webprotege.events.GetProjectEventsActionHandler;
import edu.stanford.protege.webprotege.itemlist.GetPersonIdCompletionsActionHandler;
import edu.stanford.protege.webprotege.itemlist.GetPersonIdItemsActionHandler;
import edu.stanford.protege.webprotege.itemlist.GetUserIdCompletionsActionHandler;
import edu.stanford.protege.webprotege.mail.GetEmailAddressActionHandler;
import edu.stanford.protege.webprotege.mail.SetEmailAddressActionHandler;
import edu.stanford.protege.webprotege.permissions.GetProjectPermissionsActionHandler;
import edu.stanford.protege.webprotege.permissions.ProjectPermissionsManager;
import edu.stanford.protege.webprotege.permissions.RebuildPermissionsActionHandler;
import edu.stanford.protege.webprotege.perspective.GetPerspectivesActionHandler;
import edu.stanford.protege.webprotege.perspective.PerspectivesManager;
import edu.stanford.protege.webprotege.project.*;
import edu.stanford.protege.webprotege.user.CreateUserAccountActionHandler;
import edu.stanford.protege.webprotege.user.LogOutUserActionHandler;
import edu.stanford.protege.webprotege.user.UserActivityManager;
import edu.stanford.protege.webprotege.user.UserDetailsManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-14
 */
@Configuration
public class ApplicationActionHandlerBeansConfiguration {

    @Bean
    GetAvailableProjectsActionHandler getAvailableProjectsHandler(ProjectPermissionsManager p1,
                                                                  AccessManager p2,
                                                                  UserActivityManager p3) {
        return new GetAvailableProjectsActionHandler(p1, p2, p3);
    }


    @Bean
    GetProjectDetailsActionHandler getProjectDetailsActionHandler(ProjectDetailsManager p1) {
        return new GetProjectDetailsActionHandler(p1);
    }

    @Bean
    LoadProjectActionHandler loadProjectActionHandler(ProjectDetailsManager p1,
                                                      ProjectManager p2,
                                                      AccessManager p3,
                                                      UserActivityManager p4) {
        return new LoadProjectActionHandler(p1, p2, p3, p4);
    }

    @Bean
    CreateNewProjectActionHandler createNewProjectActionHandler(ProjectManager p1,
                                                                ProjectDetailsManager p2,
                                                                AccessManager p3, UserInSessionFactory p4) {
        return new CreateNewProjectActionHandler(p1, p2, p3, p4);
    }

    @Bean
    GetProjectEventsActionHandler getProjectEventsActionHandler(ProjectManager p1, AccessManager p2) {
        return new GetProjectEventsActionHandler(p1, p2);
    }

    @Bean
    public GetCurrentUserInSessionActionHandler getCurrentUserInSessionActionHandler(UserInSessionFactory p1) {
        return new GetCurrentUserInSessionActionHandler(p1);
    }

    @Bean
    SetEmailAddressActionHandler setEmailAddressActionHandler(UserDetailsManager p1) {
        return new SetEmailAddressActionHandler(p1);
    }

    @Bean
    GetEmailAddressActionHandler getEmailAddressActionHandler(UserDetailsManager p1) {
        return new GetEmailAddressActionHandler(p1);
    }

    @Bean
    MoveProjectsToTrashActionHandler moveProjectsToTrashActionHandler(ProjectDetailsManager p1) {
        return new MoveProjectsToTrashActionHandler(p1);
    }

    @Bean
    public ApplicationActionHandler removeProjectsFromTrashActionHandler(ProjectDetailsManager p1) {
        return new RemoveProjectsFromTrashActionHandler(p1);
    }

    @Bean
    ResetPasswordActionHandler resetPasswordActionHandler(UserDetailsManager p1,
                                                          AuthenticationManager p2,
                                                          ResetPasswordMailer p3) {
        return new ResetPasswordActionHandler(p1, p2, p3);
    }

    @Bean
    LogOutUserActionHandler logOutUserActionHandler(UserActivityManager p1, UserInSessionFactory p2) {
        return new LogOutUserActionHandler(p1, p2);
    }

    @Bean
    PerformLoginActionHandler performLoginActionHandler(UserActivityManager p1,
                                                        UserInSessionFactory p2,
                                                        AuthenticationManager p3) {
        return new PerformLoginActionHandler(p1, p2, p3);
    }

    @Bean
    ChangePasswordActionHandler changePasswordActionHandler(AuthenticationManager p1) {
        return new ChangePasswordActionHandler(p1);
    }

    @Bean
    CreateUserAccountActionHandler createUserAccountActionHandler(AccessManager p1, AuthenticationManager p2) {
        return new CreateUserAccountActionHandler(p1, p2);
    }

    @Bean
    GetProjectPermissionsActionHandler getPermissionsActionHandler(AccessManager p1) {
        return new GetProjectPermissionsActionHandler(p1);
    }

    @Bean
    public ApplicationActionHandler getPersonIdCompletionsActionHandler(UserDetailsManager p1) {
        return new GetPersonIdCompletionsActionHandler(p1);
    }

    @Bean
    GetUserIdCompletionsActionHandler getUserIdCompletionsActionHandler(UserDetailsManager p1) {
        return new GetUserIdCompletionsActionHandler(p1);
    }

    @Bean
    GetPersonIdItemsActionHandler getPersonIdItemsActionHandler(UserDetailsManager p1) {
        return new GetPersonIdItemsActionHandler(p1);
    }

    @Bean
    GetApplicationSettingsActionHandler getSystemSettingsActionHandler(AccessManager p1, ApplicationSettingsManager p2) {
        return new GetApplicationSettingsActionHandler(p1, p2);
    }

    @Bean
    SetApplicationSettingsActionHandler setAdminSettingsActionHandler(ApplicationSettingsManager p1, AccessManager p2) {
        return new SetApplicationSettingsActionHandler(p1, p2);
    }

    @Bean
    RebuildPermissionsActionHandler rebuildPermissionsActionHandler(AccessManager p1) {
        return new RebuildPermissionsActionHandler(p1);
    }

    @Bean
    public GetAvailableProjectsWithPermissionActionHandler getAvailableProjectsWithPermissionActionHandler(AccessManager p1,
                                                                                     ProjectDetailsManager p2) {
        return new GetAvailableProjectsWithPermissionActionHandler(p1, p2);
    }

    @Bean
    GetPerspectivesActionHandler getPerspectivesActionHandler(PerspectivesManager p1) {
        return new GetPerspectivesActionHandler(p1);
    }
}
