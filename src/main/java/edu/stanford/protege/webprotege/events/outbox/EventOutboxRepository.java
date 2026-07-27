package edu.stanford.protege.webprotege.events.outbox;

import org.bson.types.ObjectId;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Persistence operations for the durable event outbox.
 */
public interface EventOutboxRepository {

    /**
     * Inserts a new outbox record.  Failures propagate so that a change can never be recorded without its
     * corresponding outbox row.
     */
    void insert(@Nonnull EventOutboxRecord record);

    /**
     * Gets the ids of all projects that have at least one record that has not yet been sent (i.e. a record
     * in the {@link EventOutboxState#GATED} or {@link EventOutboxState#PENDING} state).
     */
    @Nonnull
    List<String> findProjectIdsWithUnsentRecords();

    /**
     * Gets the not-yet-sent records for a project in insertion (natural {@code _id}) order.
     */
    @Nonnull
    List<EventOutboxRecord> findUnsentRecordsForProjectInOrder(@Nonnull String projectId);

    /**
     * Promotes {@link EventOutboxState#GATED} records for a project whose revision number is less than or
     * equal to the supplied durable-revision watermark to {@link EventOutboxState#PENDING}.
     *
     * @return the number of records promoted.
     */
    long promoteGatedRecordsUpToRevision(@Nonnull String projectId, long durableRevision);

    /**
     * Marks a record as {@link EventOutboxState#SENT}.
     */
    void markSent(@Nonnull ObjectId id);

    /**
     * Records a failed publish attempt, incrementing the attempt count and stamping the attempt time.
     */
    void recordFailedAttempt(@Nonnull ObjectId id, @Nonnull Instant attemptedAt);

    /**
     * Gets the creation time of the oldest record still awaiting publication ({@link EventOutboxState#PENDING}
     * or {@link EventOutboxState#GATED}), for liveness monitoring.
     */
    @Nonnull
    Optional<Instant> findOldestUnsentRecordCreationTime();
}
