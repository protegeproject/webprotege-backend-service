package edu.stanford.protege.webprotege;

import dagger.Provides;
import dagger.multibindings.IntoSet;
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
import edu.stanford.protege.webprotege.csv.GetCSVGridActionHandler;
import edu.stanford.protege.webprotege.dispatch.ApplicationActionHandler;
import edu.stanford.protege.webprotege.dispatch.handlers.*;
import edu.stanford.protege.webprotege.events.GetProjectEventsActionHandler;
import edu.stanford.protege.webprotege.inject.UploadsDirectory;
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

import java.io.File;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-07
 */
@Configuration
public class ApplicationActionHandlerConfiguration {


    @Bean
    public ApplicationActionHandler provideGetAvailableProjectsHandler(ProjectPermissionsManager projectPermissionsHandler,
                                                                       AccessManager accessManager,
                                                                       UserActivityManager userActivityManager) {
        return new GetAvailableProjectsHandler(projectPermissionsHandler,
                                               accessManager,
                                               userActivityManager);
    }

    @Bean
    public ApplicationActionHandler provideGetProjectDetailsActionHandler(ProjectDetailsManager projectDetailsManager) {
        return new GetProjectDetailsActionHandler(projectDetailsManager);
    }

    @Bean
    public ApplicationActionHandler provideLoadProjectActionHandler(ProjectDetailsManager projectDetailsManager,
                                                                    ProjectManager projectManager,
                                                                    AccessManager accessManager,
                                                                    UserActivityManager userActivityManager) {
        return new LoadProjectActionHandler(projectDetailsManager,
                                            projectManager,
                                            accessManager,
                                            userActivityManager);
    }

    @Bean
    public ApplicationActionHandler provideCreateNewProjectActionHandler(ProjectManager projectManager,
                                                                         ProjectDetailsManager projectDetailsManager,
                                                                         AccessManager accessManager,
                                                                         UserInSessionFactory userInSessionFactory) {
        return new CreateNewProjectActionHandler(projectManager,
                                                 projectDetailsManager,
                                                 accessManager,
                                                 userInSessionFactory);
    }

    @Bean
    public ApplicationActionHandler provideGetProjectEventsActionHandler(ProjectManager projectManager,
                                                                         AccessManager accessManager) {
        return new GetProjectEventsActionHandler(projectManager,
                                                 accessManager);
    }

    @Bean
    public ApplicationActionHandler provideGetCurrentUserInSessionActionHandler(UserInSessionFactory userInSessionFactory) {
        return new GetCurrentUserInSessionActionHandler(userInSessionFactory);
    }

    @Bean
    public ApplicationActionHandler provideSetEmailAddressActionHandler(UserDetailsManager userDetailsManager) {
        return new SetEmailAddressActionHandler(userDetailsManager);
    }

    @Bean
    public ApplicationActionHandler provideGetEmailAddressActionHandler(UserDetailsManager userDetailsManager) {
        return new GetEmailAddressActionHandler(userDetailsManager);
    }

    @Bean
    public ApplicationActionHandler provideMoveProjectsToTrashActionHandler(ProjectDetailsManager projectDetailsManager) {
        return new MoveProjectsToTrashActionHandler(projectDetailsManager);
    }

    @Bean
    public ApplicationActionHandler provideRemoveProjectsFromTrashActionHandler(ProjectDetailsManager projectDetailsManager) {
        return new RemoveProjectsFromTrashActionHandler(projectDetailsManager);
    }

    @UploadsDirectory
    @Bean
    File getUploadsDirectory() {
        return new File("/tmp/uploads");
    }

    @Bean
    public ApplicationActionHandler provideGetCSVGridActionHandler(@UploadsDirectory File uploadsDirectory) {
        return new GetCSVGridActionHandler(uploadsDirectory);
    }

    @Bean
    public ApplicationActionHandler provideResetPasswordActionHandler(UserDetailsManager userDetailsManager,
                                                                      AuthenticationManager authenticationManager,
                                                                      ResetPasswordMailer resetPasswordMailer) {
        return new ResetPasswordActionHandler(userDetailsManager,
                                              authenticationManager,
                                              resetPasswordMailer);
    }

    @Bean
    public ApplicationActionHandler provideLogOutUserActionHandler(UserActivityManager userActivityManager,
                                                                   UserInSessionFactory userInSessionFactory) {
        return new LogOutUserActionHandler(userActivityManager, userInSessionFactory);
    }

    @Bean
    public ApplicationActionHandler providePerformLoginActionHandler(UserActivityManager userActivityManager,
                                                                     UserInSessionFactory userInSessionFactory,
                                                                     AuthenticationManager authenticationManager) {
        return new PerformLoginActionHandler(userActivityManager,
                                             userInSessionFactory,
                                             authenticationManager);
    }

    @Bean
    public ApplicationActionHandler provideChangePasswordActionHandler(AuthenticationManager authenticationManager) {
        return new ChangePasswordActionHandler(authenticationManager);
    }

    @Bean
    public ApplicationActionHandler provideCreateUserAccountActionHandler(AccessManager accessManager,
                                                                          AuthenticationManager authenticationManager) {
        return new CreateUserAccountActionHandler(accessManager, authenticationManager);
    }

    @Bean
    public ApplicationActionHandler provideGetPermissionsActionHandler(AccessManager accessManager) {
        return new GetProjectPermissionsActionHandler(accessManager);
    }

    @Bean
    public ApplicationActionHandler provideGetPersonIdCompletionsActionHandler(UserDetailsManager userDetailsManager) {
        return new GetPersonIdCompletionsActionHandler(userDetailsManager);
    }

    @Bean
    public ApplicationActionHandler provideGetUserIdCompletionsActionHandler(UserDetailsManager userDetailsManager) {
        return new GetUserIdCompletionsActionHandler(userDetailsManager);
    }

    @Bean
    public ApplicationActionHandler provideGetPersonIdItemsActionHandler(UserDetailsManager userDetailsManager) {
        return new GetPersonIdItemsActionHandler(userDetailsManager);
    }

    @Bean
    public ApplicationActionHandler provideGetSystemSettingsActionHandler(AccessManager accessManager,
                                                                          ApplicationSettingsManager applicationSettingsManager) {
        return new GetApplicationSettingsActionHandler(accessManager,
                                                       applicationSettingsManager);
    }

    @Bean
    public ApplicationActionHandler provideSetAdminSettingsActionHandler(ApplicationSettingsManager applicationSettingsManager,
                                                                         AccessManager accessManager) {
        return new SetApplicationSettingsActionHandler(applicationSettingsManager, accessManager);
    }

    @Bean
    public ApplicationActionHandler providesRebuildPermissionsActionHandler(AccessManager accessManager) {
        return new RebuildPermissionsActionHandler(accessManager);
    }

    @Bean
    public ApplicationActionHandler providesGetAvailableProjectsWithPermissionActionHandler(
             AccessManager accessManager,
             ProjectDetailsManager projectDetailsManager) {
        return new GetAvailableProjectsWithPermissionActionHandler(accessManager, projectDetailsManager);
    }


    @Bean
    public ApplicationActionHandler provideGetPerspectivesActionHandler(PerspectivesManager perspectivesManager) {
        return new GetPerspectivesActionHandler(perspectivesManager);
    }

}
