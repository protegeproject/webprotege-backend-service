package edu.stanford.protege.webprotege.project;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.UUID;

/**
 * A unique identifier for a project history replacement (see @{@link ReplaceProjectHistoryActionHandler}
 * <p>
 * This ID distinguishes between different operations that attempt to
 * replace the revision history of a project. It ensures that events
 * related to a specific replacement operation can be correlated.
 * </p>
 * <p>
 * Instances of this class are immutable and can be serialized/deserialized
 * using Jackson for transfer over the wire.
 * </p>
 *
 * @param id The unique identifier string for this operation. Must not be {@code null}.
 */
public record ProjectHistoryReplacementOperationId(@JsonValue String id) {

    /**
     * Creates a {@link ProjectHistoryReplacementOperationId} record.
     *
     * @param id The identifier string. Must not be {@code null}.
     * @throws NullPointerException if {@code id} is {@code null}.
     */
    public ProjectHistoryReplacementOperationId {
        Objects.requireNonNull(id);
    }

    /**
     * Creates a {@link ProjectHistoryReplacementOperationId} from the given string value.
     *
     * @param id The string identifier. Must not be {@code null}.
     * @return A {@link ProjectHistoryReplacementOperationId} wrapping the given string.
     * @throws NullPointerException if {@code id} is {@code null}.
     */
    @JsonCreator
    @Nonnull
    public static ProjectHistoryReplacementOperationId valueOf(String id) {
        return new ProjectHistoryReplacementOperationId(id);
    }

    /**
     * Generates a new {@link ProjectHistoryReplacementOperationId}
     * with a randomly created UUID string.
     *
     * @return A newly generated {@link ProjectHistoryReplacementOperationId}.
     */
    @Nonnull
    public static ProjectHistoryReplacementOperationId generate() {
        return valueOf(UUID.randomUUID().toString());
    }
}
