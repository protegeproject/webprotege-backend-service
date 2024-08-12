package edu.stanford.protege.webprotege.forms;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.common.ValueObject;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.UUID;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */

@AutoValue
public abstract class FormId implements Serializable, ValueObject {

    @JsonCreator
    public static FormId get(@Nonnull String id) {
        checkFormat(id);
        return new AutoValue_FormId(id);
    }

    @Nonnull
    public static FormId valueOf(@Nonnull String id) {
        return get(id);
    }

    public static FormId generate() {
        return get(UUID.randomUUID().toString());
    }

    public static void checkFormat(@Nonnull String id) {
        UUID.fromString(id);
    }

    @Override
    public String value() {
        return getId();
    }

    @JsonValue
    public abstract String getId();
}
