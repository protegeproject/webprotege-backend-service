package edu.stanford.protege.webprotege.init;

import com.mongodb.MongoClient;
import com.mongodb.MongoTimeoutException;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 10/04/2013
 */
public class CheckMongoDBConnectionTask implements ConfigurationTask {

    @Nonnull
    private final MongoClient mongoClient;

    @Inject
    public CheckMongoDBConnectionTask(@Nonnull MongoClient mongoClient) {
        this.mongoClient = checkNotNull(mongoClient);
    }

    @Override
    public void run() throws WebProtegeConfigurationException {
        try {
            mongoClient.listDatabaseNames().first();
        } catch (MongoTimeoutException e) {
            throw new WebProtegeConfigurationException(getUnknownHostErrorMessage());
        }
    }

    private String getUnknownHostErrorMessage() {
        return "Could not connect to MongoDB";
    }

}
