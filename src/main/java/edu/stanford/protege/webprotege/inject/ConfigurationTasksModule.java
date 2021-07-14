package edu.stanford.protege.webprotege.inject;

import edu.stanford.protege.webprotege.init.CheckDataDirectoryIsReadableAndWritable;
import edu.stanford.protege.webprotege.init.CheckWebProtegeDataDirectoryExists;
import edu.stanford.protege.webprotege.init.ConfigurationTask;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public class ConfigurationTasksModule {

    public ConfigurationTask provideCheckWebProtegeDataDirectoryExists(CheckWebProtegeDataDirectoryExists check) {
        return check;
    }

    public ConfigurationTask provideCheckDataDirectoryIsReadableAndWritable(CheckDataDirectoryIsReadableAndWritable check) {
        return check;
    }
}
