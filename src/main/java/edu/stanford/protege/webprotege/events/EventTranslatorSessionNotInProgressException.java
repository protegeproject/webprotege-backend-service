package edu.stanford.protege.webprotege.events;

public class EventTranslatorSessionNotInProgressException extends RuntimeException {

    public EventTranslatorSessionNotInProgressException() {
        super("No event translator session in progress");
    }
}
