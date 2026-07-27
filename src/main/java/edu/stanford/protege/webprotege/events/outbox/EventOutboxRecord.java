package edu.stanford.protege.webprotege.events.outbox;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Instant;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A durable record of a project change event that must be published to other services.  Rows are drained
 * per project in insertion order (natural {@code _id} order) by {@link EventOutboxRelay}.
 */
@Document(collection = EventOutboxRecord.COLLECTION)
@CompoundIndexes({
        @CompoundIndex(name = "projectId_state", def = "{'projectId': 1, 'state': 1, '_id': 1}")
})
public class EventOutboxRecord {

    public static final String COLLECTION = "EventOutbox";

    public static final String PROJECT_ID = "projectId";

    public static final String REVISION_NUMBER = "revisionNumber";

    public static final String STATE = "state";

    public static final String ATTEMPTS = "attempts";

    public static final String LAST_ATTEMPT_AT = "lastAttemptAt";

    public static final String CREATED_AT = "createdAt";

    @Id
    private final ObjectId id;

    private final String projectId;

    private final long revisionNumber;

    private final String channel;

    private final String payload;

    private final EventOutboxState state;

    private final int attempts;

    @Nullable
    private final Instant lastAttemptAt;

    private final Instant createdAt;

    @PersistenceCreator
    public EventOutboxRecord(@Nullable ObjectId id,
                             @Nonnull String projectId,
                             long revisionNumber,
                             @Nonnull String channel,
                             @Nonnull String payload,
                             @Nonnull EventOutboxState state,
                             int attempts,
                             @Nullable Instant lastAttemptAt,
                             @Nonnull Instant createdAt) {
        this.id = id;
        this.projectId = checkNotNull(projectId);
        this.revisionNumber = revisionNumber;
        this.channel = checkNotNull(channel);
        this.payload = checkNotNull(payload);
        this.state = checkNotNull(state);
        this.attempts = attempts;
        this.lastAttemptAt = lastAttemptAt;
        this.createdAt = checkNotNull(createdAt);
    }

    /**
     * Creates a fresh record to be inserted.  The {@code _id} is left {@code null} so that Mongo mints a
     * monotonically increasing {@link ObjectId} at insert time, which establishes the per-project ordering.
     */
    public static EventOutboxRecord create(@Nonnull String projectId,
                                           long revisionNumber,
                                           @Nonnull String channel,
                                           @Nonnull String payload,
                                           @Nonnull EventOutboxState state,
                                           @Nonnull Instant createdAt) {
        return new EventOutboxRecord(null, projectId, revisionNumber, channel, payload, state, 0, null, createdAt);
    }

    @Nullable
    public ObjectId getId() {
        return id;
    }

    @Nonnull
    public String getProjectId() {
        return projectId;
    }

    public long getRevisionNumber() {
        return revisionNumber;
    }

    @Nonnull
    public String getChannel() {
        return channel;
    }

    @Nonnull
    public String getPayload() {
        return payload;
    }

    @Nonnull
    public EventOutboxState getState() {
        return state;
    }

    public int getAttempts() {
        return attempts;
    }

    @Nullable
    public Instant getLastAttemptAt() {
        return lastAttemptAt;
    }

    @Nonnull
    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return toStringHelper("EventOutboxRecord")
                .add("id", id)
                .add("projectId", projectId)
                .add("revisionNumber", revisionNumber)
                .add("channel", channel)
                .add("state", state)
                .add("attempts", attempts)
                .toString();
    }
}
