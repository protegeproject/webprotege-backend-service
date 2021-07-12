package edu.stanford.protege.webprotege.app;

import dagger.Component;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.api.ApiModule;
import edu.stanford.protege.webprotege.auth.AuthenticationServlet;
import edu.stanford.protege.webprotege.download.ProjectDownloadServlet;
import edu.stanford.protege.webprotege.inject.*;
import edu.stanford.protege.webprotege.inject.project.ProjectModule;
import edu.stanford.protege.webprotege.project.ProjectCacheManager;
import edu.stanford.protege.webprotege.upload.FileUploadServlet;
import edu.stanford.protege.webprotege.user.UserDetailsManager;
import edu.stanford.protege.webprotege.inject.ApplicationSingleton;
import edu.stanford.protege.webprotege.inject.SharedApplicationModule;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Jan 2018
 */
//@Component(modules = {
//        ApplicationModule.class,
//        SharedApplicationModule.class,
//        FileSystemConfigurationModule.class,
//        ConfigurationTasksModule.class,
//        ActionHandlersModule.class,
//        AuthenticationModule.class,
//        DbModule.class,
//        ServerModule.class,
//        ApiModule.class
//})
@ApplicationSingleton
public interface ServerComponent {

    ApplicationNameSupplier getApplicationNameSupplier();

    ApplicationSettingsChecker getApplicationSettingsChecker();

    UserDetailsManager getUserDetailsManager();

    AccessManager getAccessManager();

    FileUploadServlet getFileUploadServlet();

    ServletContainer getJerseyServletContainer();

    ProjectDownloadServlet getProjectDownloadServlet();

    AuthenticationServlet getAuthenticationServlet();

    WebProtegeSessionListener getSessionListener();

    WebProtegeConfigurationChecker getWebProtegeConfigurationChecker();

    ProjectComponent getProjectComponent(ProjectModule module);

    ApplicationDisposablesManager getApplicationDisposablesManager();

    ProjectCacheManager getProjectCacheManager();

}
