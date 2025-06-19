package edu.stanford.protege.webprotege.events;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EventTranslatorSessionCheckerTest {

    private EventTranslatorSessionChecker checker;

    private EventTranslatorSessionId sessionId1;

    private EventTranslatorSessionId sessionId2;

    @BeforeEach
    public void setUp() {
        checker = new EventTranslatorSessionChecker();
        sessionId1 = new EventTranslatorSessionId("session-1");
        sessionId2 = new EventTranslatorSessionId("session-2");
    }

    @Test
    public void startSession_shouldSucceed_whenNoSessionInProgress() {
        assertDoesNotThrow(() -> checker.startSession(sessionId1));
    }

    @Test
    public void startSession_shouldThrow_whenSessionAlreadyInProgress() {
        checker.startSession(sessionId1);
        assertThrows(EventTranslatorSessionAlreadyInProgressException.class, () -> checker.startSession(sessionId2));
    }

    @Test
    public void startSession_shouldThrow_whenNullSessionId() {
        assertThrows(IllegalArgumentException.class, () -> checker.startSession(null));
    }

    @Test
    public void finishSession_shouldSucceed_whenSessionMatches() {
        checker.startSession(sessionId1);
        assertDoesNotThrow(() -> checker.finishSession(sessionId1));
    }

    @Test
    public void finishSession_shouldThrow_whenNoSessionInProgress() {
        assertThrows(EventTranslatorSessionNotInProgressException.class, () -> checker.finishSession(sessionId1));
    }

    @Test
    public void finishSession_shouldThrow_whenSessionIdDoesNotMatch() {
        checker.startSession(sessionId1);
        assertThrows(EventTranslatorSessionMismatchException.class, () -> checker.finishSession(sessionId2));
    }

    @Test
    public void finishSession_shouldThrow_whenNullSessionId() {
        checker.startSession(sessionId1);
        assertThrows(IllegalArgumentException.class, () -> checker.finishSession(null));
    }
}