package edu.stanford.protege.webprotege.init;

import edu.stanford.protege.webprotege.inject.DataDirectory;

import jakarta.inject.Inject;
import java.io.File;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 10/04/2013
 */
public class CheckWebProtegeDataDirectoryExists implements ConfigurationTask {

    private final File dataDirectory;


    @Inject
    public CheckWebProtegeDataDirectoryExists(@DataDirectory File dataDirectory) {
        this.dataDirectory = checkNotNull(dataDirectory);
    }

    @Override
    public void run() throws WebProtegeConfigurationException {
       if(!dataDirectory.exists()) {
           throw new WebProtegeConfigurationException(
                   getDataDirectoryDoesNotExistsMessage(dataDirectory)
           );
       }
    }

    public static String getDataDirectoryDoesNotExistsMessage(File dataDirectory) {
        return "The WebProtege data directory cannot be found " +
                "or WebProtege does not have permission to read this directory.  " +
                "WebProtege expected the data directory to be located at " +
                dataDirectory.getAbsolutePath() +
                ".  Please check that the specified data directory exists and that " +
                "the user which the servlet container (tomcat) runs under has read " +
                "permission for this directory and its contents.";
    }
}
