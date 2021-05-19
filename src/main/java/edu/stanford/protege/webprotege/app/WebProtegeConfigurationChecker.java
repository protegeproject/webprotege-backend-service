package edu.stanford.protege.webprotege.app;

import edu.stanford.protege.webprotege.init.ConfigurationTask;
import edu.stanford.protege.webprotege.init.WebProtegeConfigurationException;

import javax.inject.Inject;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/04/2013
 */
public class WebProtegeConfigurationChecker {

    private final Set<ConfigurationTask> configurationTasks;

    @Inject
    public WebProtegeConfigurationChecker(Set<ConfigurationTask> configurationTasks) {
        this.configurationTasks = configurationTasks;
    }

    public boolean performConfiguration() throws WebProtegeConfigurationException {

        for(ConfigurationTask task : configurationTasks) {
            task.run();
        }

        return true;
    }


}
