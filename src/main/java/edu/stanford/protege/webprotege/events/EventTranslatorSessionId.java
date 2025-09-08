package edu.stanford.protege.webprotege.events;

import java.util.UUID;

public record EventTranslatorSessionId(String id) {

    public static EventTranslatorSessionId create() {
        return new EventTranslatorSessionId(UUID.randomUUID().toString());
    }
}
