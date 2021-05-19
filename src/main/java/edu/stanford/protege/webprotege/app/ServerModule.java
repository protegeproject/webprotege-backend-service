package edu.stanford.protege.webprotege.app;

import dagger.Module;
import dagger.Provides;
import edu.stanford.protege.webprotege.logging.DefaultLogger;
import edu.stanford.protege.webprotege.logging.WebProtegeLogger;
import edu.stanford.protege.webprotege.project.ProjectComponentFactory;
import edu.stanford.protege.webprotege.inject.ApplicationSingleton;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Jan 2018
 */
@Module
public class ServerModule {


    @ApplicationSingleton
    @Provides
    public WebProtegeLogger provideWebProtegeLogger(DefaultLogger logger) {
        return logger;
    }


    @Provides
    public ProjectComponentFactory provideProjectComponentFactory(ProjectComponentFactoryImpl impl) {
        return impl;
    }
}
