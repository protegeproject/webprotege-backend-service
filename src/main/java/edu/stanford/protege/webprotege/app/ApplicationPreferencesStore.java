package edu.stanford.protege.webprotege.app;

import edu.stanford.protege.webprotege.persistence.Repository;
import edu.stanford.protege.webprotege.inject.ApplicationSingleton;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.UpdateDefinition;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Mar 2017
 */
@ApplicationSingleton
public class ApplicationPreferencesStore implements Repository {

    private final MongoTemplate mongoTemplate;

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final Lock readLock = readWriteLock.readLock();

    private final Lock writeLock = readWriteLock.writeLock();

    @Nullable
    private ApplicationPreferences cachedPreferences = null;

    @Inject
    public ApplicationPreferencesStore(@Nonnull MongoTemplate mongoTemplate) {
        this.mongoTemplate = checkNotNull(mongoTemplate);
    }

    @PostConstruct
    @Override
    public void ensureIndexes() {
    }

    @Nonnull
    public ApplicationPreferences getApplicationPreferences() {
        if(cachedPreferences != null) {
            return cachedPreferences;
        }
        readLock.lock();
        try {
            var applicationPreferences = mongoTemplate.findById(ApplicationPreferences.ID, ApplicationPreferences.class);
            if (applicationPreferences == null) {
                var newApplicationPreferences = DefaultApplicationPreferences.get();
                mongoTemplate.upsert(queryById(),
                                     new Update(), ApplicationPreferences.class);
                cachedPreferences = newApplicationPreferences;
            }
            else {
                cachedPreferences = applicationPreferences;
            }
            return cachedPreferences;
        } finally {
            readLock.unlock();
        }

    }

    private Query queryById() {
        return query(where("_id").is(ApplicationPreferences.ID));
    }

    public void setApplicationPreferences(@Nonnull ApplicationPreferences preferences) {
        writeLock.lock();
        try {
            cachedPreferences = preferences;
            mongoTemplate.bulkOps(BulkOperations.BulkMode.ORDERED, ApplicationPreferences.class)
                         .remove(queryById())
                         .insert(preferences)
                         .execute();
        } finally {
            writeLock.unlock();
        }
    }
}
