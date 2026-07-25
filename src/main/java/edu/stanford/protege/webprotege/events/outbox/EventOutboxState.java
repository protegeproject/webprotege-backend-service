package edu.stanford.protege.webprotege.events.outbox;

/**
 * The lifecycle state of an {@link EventOutboxRecord}.
 */
public enum EventOutboxState {

    /**
     * The row belongs to a revision-bearing change whose change-history write is not yet known to be
     * durable.  It must not be published until the project's durable-revision watermark reaches the
     * row's revision number.
     */
    GATED,

    /**
     * The row is ready to be published and is awaiting the relay.
     */
    PENDING,

    /**
     * The row has been handed to the event dispatcher successfully.
     */
    SENT
}
