package edu.stanford.protege.webprotege.events;

public class EventTranslatorSessionChecker {

    private EventTranslatorSessionId sessionId = null;

    public synchronized void startSession(EventTranslatorSessionId sessionId) {
        if (sessionId == null) {
            throw new IllegalArgumentException("sessionId cannot be null");
        }
        if(this.sessionId != null) {
            throw new EventTranslatorSessionAlreadyInProgressException();
        }
        this.sessionId = sessionId;
    }

    public synchronized void finishSession(EventTranslatorSessionId sessionId) {
        if (sessionId == null) {
            throw new IllegalArgumentException("sessionId cannot be null");
        }
        if(this.sessionId == null) {
            throw new EventTranslatorSessionNotInProgressException();
        }
        if(!sessionId.equals(this.sessionId)) {
            throw new EventTranslatorSessionMismatchException();
        }
        this.sessionId = null;
    }
}
