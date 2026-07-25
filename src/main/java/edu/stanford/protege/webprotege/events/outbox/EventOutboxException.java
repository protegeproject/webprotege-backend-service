package edu.stanford.protege.webprotege.events.outbox;

/**
 * Thrown when a project change event cannot be written to the durable outbox.  Propagating this failure
 * ensures a change is never recorded without an outbox row that will eventually announce it.
 */
public class EventOutboxException extends RuntimeException {

    public EventOutboxException(String message, Throwable cause) {
        super(message, cause);
    }
}
