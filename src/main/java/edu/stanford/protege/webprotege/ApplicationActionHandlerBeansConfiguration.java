package edu.stanford.protege.webprotege;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.app.ApplicationSettingsManager;
import edu.stanford.protege.webprotege.app.GetApplicationSettingsActionHandler;
import edu.stanford.protege.webprotege.app.SetApplicationSettingsActionHandler;
import edu.stanford.protege.webprotege.dispatch.ApplicationActionHandler;
import edu.stanford.protege.webprotege.dispatch.handlers.*;
import edu.stanford.protege.webprotege.user.GetAuthenticatedUserDetailsActionHandler;
import edu.stanford.protege.webprotege.user.GetUserIdCompletionsActionHandler;
import edu.stanford.protege.webprotege.mail.GetEmailAddressActionHandler;
import edu.stanford.protege.webprotege.mail.SetEmailAddressActionHandler;
import edu.stanford.protege.webprotege.permissions.GetProjectPermissionsActionHandler;
import edu.stanford.protege.webprotege.permissions.RebuildPermissionsActionHandler;
import edu.stanford.protege.webprotege.perspective.GetPerspectivesActionHandler;
import edu.stanford.protege.webprotege.perspective.PerspectivesManager;
import edu.stanford.protege.webprotege.project.*;
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
    SetEmailAddressActionHandler setEmailAddressActionHandler(UserDetailsManager p1) {
        return new SetEmailAddressActionHandler(p1);
    }

    @Bean
    GetEmailAddressActionHandler getEmailAddressActionHandler(UserDetailsManager p1) {
        return new GetEmailAddressActionHandler(p1);
    }

    @Bean
    MoveProjectToTrashActionHandler moveProjectsToTrashActionHandler(ProjectDetailsManager p1) {
        return new MoveProjectToTrashActionHandler(p1);
    }

    @Bean
    public ApplicationActionHandler removeProjectsFromTrashActionHandler(ProjectDetailsManager p1) {
        return new RemoveProjectFromTrashActionHandler(p1);
    }

    @Bean
    GetProjectPermissionsActionHandler getPermissionsActionHandler(AccessManager p1) {
        return new GetProjectPermissionsActionHandler(p1);
    }

    @Bean
    GetUserIdCompletionsActionHandler getUserIdCompletionsActionHandler(UserDetailsManager p1) {
        return new GetUserIdCompletionsActionHandler(p1);
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

    @Bean
    GetAuthenticatedUserDetailsActionHandler getAuthenticatedUserDetailsActionHandler(AccessManager p1,
                                                                                      UserDetailsManager p2) {
        return new GetAuthenticatedUserDetailsActionHandler(p1, p2);
    }
}
