package edu.stanford.protege.webprotege.ui;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.UUID;

public record ViewId(@JsonValue String value) {

    public ViewId(String value) {
        this.value = Objects.requireNonNull(value, "value cannot be null");
    }

    @JsonCreator
    public static ViewId valueOf(String id) {
        return new ViewId(id);
    }

    @Nonnull
    public static ViewId generate() {
        return new ViewId(UUID.randomUUID().toString());
    }
}
