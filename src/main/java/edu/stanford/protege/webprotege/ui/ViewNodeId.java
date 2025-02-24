package edu.stanford.protege.webprotege.ui;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;
import java.util.UUID;

public record ViewNodeId(@JsonValue String value) {

    public ViewNodeId(String value) {
        this.value = Objects.requireNonNull(value, "value cannot be null");
    }

    @JsonCreator
    public static ViewNodeId valueOf(String value) {
        return new ViewNodeId(value);
    }

    public static ViewNodeId generate() {
        return new ViewNodeId(UUID.randomUUID().toString());
    }
}
