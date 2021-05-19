package edu.stanford.protege.webprotege.app;

import com.mongodb.MongoClient;
import edu.stanford.protege.webprotege.HasDispose;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10 Apr 2018
 */
public class DatabaseClientConnection implements HasDispose {

    @Nonnull
    private final MongoClient mongoClient;

    @Inject
    public DatabaseClientConnection(@Nonnull MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @Override
    public void dispose() {
        mongoClient.close();
    }
}
