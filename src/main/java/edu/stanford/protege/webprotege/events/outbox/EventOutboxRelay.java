package edu.stanford.protege.webprotege.events.outbox;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.ipc.Headers;
import edu.stanford.protege.webprotege.ipc.impl.RabbitMQEventsConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Drains the durable {@link EventOutboxRecord} rows, publishing each to the event exchange.
 *
 * <p>The row stores the exact bytes that were serialized when the change was applied; the relay republishes
 * those bytes with the same headers {@code RabbitMQEventDispatcher} sets, so the message on the wire is
 * identical to a direct dispatch.  (Republishing the stored bytes rather than deserializing and re-dispatching
 * is deliberate: the backend's ObjectMapper does not register the polymorphic {@code ProjectEvent} subtypes,
 * so it can serialize a {@code PackagedProjectChangeEvent} but cannot deserialize one back.)</p>
 *
 * <p>Records for a project are processed strictly in insertion order.  Processing of a project stops at the
 * first record that is still {@link EventOutboxState#GATED gated} on durability or that fails to publish, so a
 * stall or a failure never lets a later change overtake an earlier one for the same project (head-of-line
 * blocking preserves per-project ordering).  Delivery is at-least-once: a crash between a successful publish
 * and the SENT write results in a re-publish, which downstream consumers de-duplicate.</p>
 */
public class EventOutboxRelay {

    private static final Logger logger = LoggerFactory.getLogger(EventOutboxRelay.class);

    private final EventOutboxRepository repository;

    private final RabbitTemplate eventRabbitTemplate;

    private final DurableRevisionWatermarkRegistry watermarkRegistry;

    private final String applicationName;

    private final Clock clock;

    private final long baseBackoffMillis;

    private final long maxBackoffMillis;

    private final long oldestPendingWarnThresholdMillis;

    public EventOutboxRelay(@Nonnull EventOutboxRepository repository,
                            @Nonnull RabbitTemplate eventRabbitTemplate,
                            @Nonnull DurableRevisionWatermarkRegistry watermarkRegistry,
                            @Nonnull String applicationName,
                            @Nonnull Clock clock,
                            long baseBackoffMillis,
                            long maxBackoffMillis,
                            long oldestPendingWarnThresholdMillis) {
        this.repository = checkNotNull(repository);
        this.eventRabbitTemplate = checkNotNull(eventRabbitTemplate);
        this.watermarkRegistry = checkNotNull(watermarkRegistry);
        this.applicationName = checkNotNull(applicationName);
        this.clock = checkNotNull(clock);
        this.baseBackoffMillis = baseBackoffMillis;
        this.maxBackoffMillis = maxBackoffMillis;
        this.oldestPendingWarnThresholdMillis = oldestPendingWarnThresholdMillis;
    }

    public void drainOutbox() {
        for (var projectId : repository.findProjectIdsWithUnsentRecords()) {
            try {
                drainProject(ProjectId.valueOf(projectId));
            } catch (RuntimeException e) {
                // Isolate projects from one another: a failure draining one project must not prevent the
                // others from being drained on this cycle.
                logger.error("[EventOutboxRelay] Error draining outbox for project {}", projectId, e);
            }
        }
        reportOldestUnsentAge();
    }

    private void drainProject(ProjectId projectId) {
        watermarkRegistry.getDurableRevision(projectId)
                .ifPresent(durableRevision -> repository.promoteGatedRecordsUpToRevision(projectId.value(), durableRevision));

        for (var record : repository.findUnsentRecordsForProjectInOrder(projectId.value())) {
            if (record.getState() == EventOutboxState.GATED) {
                // The change-history write is not yet durable; nothing after it may be published either.
                break;
            }
            if (isBackingOff(record)) {
                break;
            }
            if (!publish(record)) {
                // Leave this record PENDING and stop; do not publish later rows for this project.
                break;
            }
        }
    }

    private boolean publish(EventOutboxRecord record) {
        try {
            eventRabbitTemplate.send(RabbitMQEventsConfiguration.EVENT_EXCHANGE, "", toMessage(record));
        } catch (RuntimeException e) {
            logger.warn("[EventOutboxRelay] Failed to publish outbox record {} for project {} (attempt {}); will retry",
                        record.getId(), record.getProjectId(), record.getAttempts() + 1, e);
            repository.recordFailedAttempt(record.getId(), clock.instant());
            return false;
        }
        repository.markSent(record.getId());
        return true;
    }

    private Message toMessage(EventOutboxRecord record) {
        var message = MessageBuilder.withBody(record.getPayload().getBytes(StandardCharsets.UTF_8)).build();
        var headers = message.getMessageProperties().getHeaders();
        // The outbox only holds PackagedProjectChangeEvents, whose @JsonTypeName equals its channel, so the
        // channel serves as both the event-type and channel headers, matching RabbitMQEventDispatcher.
        headers.put(Headers.EVENT_TYPE, record.getChannel());
        headers.put(Headers.CHANNEL, record.getChannel());
        headers.put(Headers.PROJECT_ID, record.getProjectId());
        message.getMessageProperties().setHeader(Headers.SERVICE_NAME, applicationName);
        return message;
    }

    private boolean isBackingOff(EventOutboxRecord record) {
        var lastAttemptAt = record.getLastAttemptAt();
        if (record.getAttempts() == 0 || lastAttemptAt == null) {
            return false;
        }
        var backoff = backoffFor(record.getAttempts());
        return clock.instant().isBefore(lastAttemptAt.plusMillis(backoff));
    }

    private long backoffFor(int attempts) {
        int shift = Math.min(attempts - 1, 16);
        long backoff = baseBackoffMillis << shift;
        if (backoff <= 0 || backoff > maxBackoffMillis) {
            return maxBackoffMillis;
        }
        return backoff;
    }

    private void reportOldestUnsentAge() {
        repository.findOldestUnsentRecordCreationTime().ifPresent(oldest -> {
            var age = Duration.between(oldest, Instant.now(clock));
            if (age.toMillis() >= oldestPendingWarnThresholdMillis) {
                logger.warn("[EventOutboxRelay] Oldest unsent outbox record is {} ms old — the relay may be stalled", age.toMillis());
            } else {
                logger.debug("[EventOutboxRelay] Oldest unsent outbox record is {} ms old", age.toMillis());
            }
        });
    }
}
