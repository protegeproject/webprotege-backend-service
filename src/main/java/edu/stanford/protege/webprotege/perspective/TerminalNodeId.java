package edu.stanford.protege.webprotege.perspective;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-20
 */
@AutoValue
public abstract class TerminalNodeId {

    public static final String NODE_ID_PREFIX = "N-";

    private static int counter = 0;

    public static TerminalNodeId get() {
        String id = NODE_ID_PREFIX + nextId();
        return new AutoValue_TerminalNodeId(id);
    }

    @JsonCreator
    @Nonnull
    public static TerminalNodeId get(String id) {
        return new AutoValue_TerminalNodeId(id);
    }

    @JsonValue
    public abstract String getId();

    /**
     * Gets the next internal id.
     * @return The next internal id.
     */
    private static int nextId() {
        return counter++;
    }
}
