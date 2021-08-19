package edu.stanford.protege.webprotege.app;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.download.ProjectDownloadServlet;
import edu.stanford.protege.webprotege.inject.ApplicationSingleton;
import edu.stanford.protege.webprotege.inject.ProjectComponent;
import edu.stanford.protege.webprotege.inject.project.ProjectModule;
import edu.stanford.protege.webprotege.project.ProjectCacheManager;
import edu.stanford.protege.webprotege.upload.FileUploadServlet;
import edu.stanford.protege.webprotege.user.UserDetailsManager;
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

    WebProtegeSessionListener getSessionListener();

    WebProtegeConfigurationChecker getWebProtegeConfigurationChecker();

    ProjectComponent getProjectComponent(ProjectModule module);

    ApplicationDisposablesManager getApplicationDisposablesManager();

    ProjectCacheManager getProjectCacheManager();

}
