package edu.stanford.protege.webprotege.events;

public class EventTranslatorSessionAlreadyInProgressException extends RuntimeException {

    public EventTranslatorSessionAlreadyInProgressException() {
        super("An event translator session is already in progress.  Cannot start a new session.");
    }
}
