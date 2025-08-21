package edu.stanford.protege.webprotege.events;

public class EventTranslatorSessionMismatchException extends RuntimeException {

    public EventTranslatorSessionMismatchException() {
        super("Event translator session mismatch");
    }
}
