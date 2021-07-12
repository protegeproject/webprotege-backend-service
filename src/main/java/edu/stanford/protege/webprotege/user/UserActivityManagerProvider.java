package edu.stanford.protege.webprotege.user;

import org.springframework.data.mongodb.core.MongoTemplate;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Mar 2017
 */
public class UserActivityManagerProvider implements Provider<UserActivityManager> {


    private final MongoTemplate mongoOperations;

    @Inject
    public UserActivityManagerProvider(MongoTemplate mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public UserActivityManager get() {
        UserActivityManager userActivityManager = new UserActivityManager(mongoOperations);
        userActivityManager.ensureIndexes();
        return userActivityManager;
    }
}
