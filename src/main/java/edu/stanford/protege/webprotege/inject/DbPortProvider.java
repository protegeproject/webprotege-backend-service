package edu.stanford.protege.webprotege.inject;

import edu.stanford.protege.webprotege.app.WebProtegeProperties;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/03/15
 */
public class DbPortProvider implements Provider<Optional<Integer>> {

    private WebProtegeProperties webProtegeProperties;

    @Inject
    public DbPortProvider(WebProtegeProperties webProtegeProperties) {
        this.webProtegeProperties = checkNotNull(webProtegeProperties);
    }

    @Override
    public Optional<Integer> get() {
        Optional<String> dbPort = webProtegeProperties.getDBPort();
        return dbPort.map(Integer::parseInt);
    }
}
