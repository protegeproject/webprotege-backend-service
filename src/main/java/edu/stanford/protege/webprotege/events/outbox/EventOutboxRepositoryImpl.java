package edu.stanford.protege.webprotege.events.outbox;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.protege.webprotege.events.outbox.EventOutboxRecord.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

public class EventOutboxRepositoryImpl implements EventOutboxRepository {

    private final MongoTemplate mongoTemplate;

    @Inject
    public EventOutboxRepositoryImpl(@Nonnull MongoTemplate mongoTemplate) {
        this.mongoTemplate = checkNotNull(mongoTemplate);
    }

    @Override
    public void insert(@Nonnull EventOutboxRecord record) {
        mongoTemplate.insert(checkNotNull(record));
    }

    @Nonnull
    @Override
    public List<String> findProjectIdsWithUnsentRecords() {
        var query = query(where(STATE).in(EventOutboxState.GATED, EventOutboxState.PENDING));
        return mongoTemplate.findDistinct(query, PROJECT_ID, EventOutboxRecord.class, String.class);
    }

    @Nonnull
    @Override
    public List<EventOutboxRecord> findUnsentRecordsForProjectInOrder(@Nonnull String projectId) {
        var query = query(where(PROJECT_ID).is(projectId)
                                  .and(STATE).in(EventOutboxState.GATED, EventOutboxState.PENDING))
                .with(Sort.by(Sort.Direction.ASC, "_id"));
        return mongoTemplate.find(query, EventOutboxRecord.class);
    }

    @Override
    public long promoteGatedRecordsUpToRevision(@Nonnull String projectId, long durableRevision) {
        var query = query(where(PROJECT_ID).is(projectId)
                                  .and(STATE).is(EventOutboxState.GATED)
                                  .and(REVISION_NUMBER).lte(durableRevision));
        var update = new Update().set(STATE, EventOutboxState.PENDING);
        return mongoTemplate.updateMulti(query, update, EventOutboxRecord.class).getModifiedCount();
    }

    @Override
    public void markSent(@Nonnull ObjectId id) {
        var update = new Update().set(STATE, EventOutboxState.SENT);
        mongoTemplate.updateFirst(query(where("_id").is(id)), update, EventOutboxRecord.class);
    }

    @Override
    public void recordFailedAttempt(@Nonnull ObjectId id, @Nonnull Instant attemptedAt) {
        var update = new Update().inc(ATTEMPTS, 1).set(LAST_ATTEMPT_AT, attemptedAt);
        mongoTemplate.updateFirst(query(where("_id").is(id)), update, EventOutboxRecord.class);
    }

    @Nonnull
    @Override
    public Optional<Instant> findOldestUnsentRecordCreationTime() {
        var query = query(where(STATE).in(EventOutboxState.GATED, EventOutboxState.PENDING))
                .with(Sort.by(Sort.Direction.ASC, CREATED_AT))
                .limit(1);
        var record = mongoTemplate.findOne(query, EventOutboxRecord.class);
        return Optional.ofNullable(record).map(EventOutboxRecord::getCreatedAt);
    }
}
