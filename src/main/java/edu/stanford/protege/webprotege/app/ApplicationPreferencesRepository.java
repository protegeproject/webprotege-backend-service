package edu.stanford.protege.webprotege.app;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-07
 */
public interface ApplicationPreferencesRepository extends MongoRepository<ApplicationPreferences, String> {

}
