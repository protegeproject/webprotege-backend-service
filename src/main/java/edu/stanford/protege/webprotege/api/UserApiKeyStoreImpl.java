package edu.stanford.protege.webprotege.api;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import edu.stanford.protege.webprotege.user.UserId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.protege.webprotege.api.ApiKeyRecord.API_KEY_ID;
import static edu.stanford.protege.webprotege.api.UserApiKeys.*;
import static java.util.Collections.singletonList;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Apr 2018
 */
public class UserApiKeyStoreImpl implements UserApiKeyStore {

    private static final int INCLUDE = 1;

    @Nonnull
    private final MongoTemplate mongoTemplate;

    @Inject
    public UserApiKeyStoreImpl(@Nonnull MongoTemplate mongoTemplate) {
        this.mongoTemplate = checkNotNull(mongoTemplate);
    }

    @Override
    public void ensureIndexes() {
    }

    @Override
    public void addApiKey(@Nonnull UserId userId, @Nonnull ApiKeyRecord record) {
        var query = query(where(USER_ID).is(userId));
        var existingKeys = mongoTemplate.findOne(query, UserApiKeys.class);
        if (existingKeys == null) {
            mongoTemplate.save(new UserApiKeys(userId, singletonList(record)));
        }
        else {
            Optional<ApiKeyRecord> existingKeyRecord = existingKeys.getApiKeys().stream()
                                                                   .filter(r -> r.getApiKeyId().equals(record.getApiKeyId()))
                                                                   .findFirst();
            existingKeyRecord.ifPresent(existing -> dropApiKey(userId, existing.getApiKeyId()));
            mongoTemplate.updateFirst(query, new Update().addToSet(API_KEYS, record), UserApiKeys.class);
        }
    }

    @Override
    public void dropApiKeys(@Nonnull UserId userId) {
        var query = query(where(USER_ID).is(userId));
        mongoTemplate.findAndRemove(query, UserApiKeys.class);
    }

    @Override
    public void dropApiKey(@Nonnull UserId userId,
                           @Nonnull ApiKeyId apiKeyId) {
        var query = query(where(USER_ID).is(userId));
        mongoTemplate.updateMulti(query, new Update().pull(API_KEYS, query(where(API_KEY_ID).is(apiKeyId))), UserApiKeys.class);
    }

    @Override
    public void setApiKeys(@Nonnull UserId userId, List<ApiKeyRecord> records) {
        Set<ApiKeyId> ids = new HashSet<>();
        List<ApiKeyRecord> nonDuplicates = records.stream()
                                                  .filter(r -> ids.add(r.getApiKeyId()))
                                                  .collect(Collectors.toList());
        var query = query(where(USER_ID).is(userId));
        mongoTemplate.upsert(query, new Update().set(API_KEYS, nonDuplicates), UserApiKeys.class);
    }

    @Nonnull
    @Override
    public List<ApiKeyRecord> getApiKeys(@Nonnull UserId userId) {
        var query = query(where(USER_ID).is(userId));
        var keys = mongoTemplate.findOne(query, UserApiKeys.class);
        if (keys == null) {
            return Collections.emptyList();
        }
        return keys.getApiKeys();
    }

    @Nonnull
    @Override
    public Optional<UserId> getUserIdForApiKey(@Nonnull HashedApiKey apiKey) {
        var query = query(where(API_KEYS__API_KEY_ID).is(apiKey));
        var found = mongoTemplate.findOne(query, UserApiKeys.class);
        return Optional.ofNullable(found)
                       .map(object -> found.getUserId());
    }
}
