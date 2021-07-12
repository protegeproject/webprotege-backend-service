package edu.stanford.protege.webprotege.inject;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import edu.stanford.protege.webprotege.init.CheckDataDirectoryIsReadableAndWritable;
import edu.stanford.protege.webprotege.init.CheckWebProtegeDataDirectoryExists;
import edu.stanford.protege.webprotege.init.ConfigurationTask;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
@Module
public class ConfigurationTasksModule {

    @Provides @IntoSet
    public ConfigurationTask provideCheckWebProtegeDataDirectoryExists(CheckWebProtegeDataDirectoryExists check) {
        return check;
    }

    @Provides @IntoSet
    public ConfigurationTask provideCheckDataDirectoryIsReadableAndWritable(CheckDataDirectoryIsReadableAndWritable check) {
        return check;
    }
}
