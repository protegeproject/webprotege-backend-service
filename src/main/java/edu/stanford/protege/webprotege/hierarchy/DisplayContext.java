package edu.stanford.protege.webprotege.hierarchy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Map;

public record DisplayContext(@JsonValue Map<String, String> properties) {

    @JsonCreator
    public static DisplayContext fromMap(Map<String, String> properties) {
        return new DisplayContext(properties);
    }
}
