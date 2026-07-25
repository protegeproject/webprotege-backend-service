package edu.stanford.protege.webprotege.events.outbox;

import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Drives {@link EventOutboxRelay#drainOutbox()} on a fixed delay.  Kept separate from the relay so that the
 * relay's logic can be driven explicitly (in tests) without the background schedule; the schedule itself can
 * be switched off with {@code webprotege.events.outbox.relay-enabled=false}.
 */
public class EventOutboxRelayScheduler {

    private final EventOutboxRelay relay;

    public EventOutboxRelayScheduler(@Nonnull EventOutboxRelay relay) {
        this.relay = checkNotNull(relay);
    }

    @Scheduled(fixedDelayString = "${webprotege.events.outbox.relay-interval-ms:1000}")
    public void run() {
        relay.drainOutbox();
    }
}
