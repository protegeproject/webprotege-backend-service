package edu.stanford.protege.webprotege.events.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.protege.webprotege.project.PackagedProjectChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.time.Clock;
import java.util.OptionalLong;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Synchronously records project change events in the durable outbox in place of dispatching them directly.
 * A revision-bearing event is written in the {@link EventOutboxState#GATED} state so that it cannot be
 * published before its change-history write is durable; the {@link EventOutboxRelay} publishes it once the
 * project's durable-revision watermark reaches the row's revision number.
 */
public class EventOutbox {

    private static final Logger logger = LoggerFactory.getLogger(EventOutbox.class);

    private final EventOutboxRepository repository;

    private final ObjectMapper objectMapper;

    private final Clock clock;

    @Inject
    public EventOutbox(@Nonnull EventOutboxRepository repository,
                       @Nonnull ObjectMapper objectMapper,
                       @Nonnull Clock clock) {
        this.repository = checkNotNull(repository);
        this.objectMapper = checkNotNull(objectMapper);
        this.clock = checkNotNull(clock);
    }

    /**
     * Records a project change event for reliable delivery.
     *
     * @param event          the event to deliver.
     * @param revisionNumber the revision number the event announces, if the change is revision-bearing.  When
     *                       present the row is gated on durability; when absent it is immediately publishable.
     * @throws EventOutboxException if the event cannot be serialized or persisted; the caller must let this
     *                              propagate so that the change fails loudly rather than being silently lost.
     */
    public void enqueue(@Nonnull PackagedProjectChangeEvent event, @Nonnull OptionalLong revisionNumber) {
        checkNotNull(event);
        var state = revisionNumber.isPresent() ? EventOutboxState.GATED : EventOutboxState.PENDING;
        var payload = serialize(event);
        var record = EventOutboxRecord.create(event.projectId().value(),
                                              revisionNumber.orElse(0L),
                                              event.getChannel(),
                                              payload,
                                              state,
                                              clock.instant());
        try {
            repository.insert(record);
        } catch (RuntimeException e) {
            throw new EventOutboxException("Could not write project change event to the outbox: " + event, e);
        }
        logger.info("[EventOutbox] Enqueued {} for project {} as {} (revision {})",
                    event.getChannel(), event.projectId().value(), state, revisionNumber.isPresent() ? revisionNumber.getAsLong() : "n/a");
    }

    private String serialize(PackagedProjectChangeEvent event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new EventOutboxException("Could not serialize project change event: " + event, e);
        }
    }
}
