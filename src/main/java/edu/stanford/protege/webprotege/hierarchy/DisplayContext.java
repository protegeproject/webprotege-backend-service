package edu.stanford.protege.webprotege.hierarchy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Map;

public record DisplayContext(@JsonProperty("properties") Map<String, String> properties) {

    @JsonCreator
    public static DisplayContext fromMap(@JsonProperty("properties") Map<String, String> properties) {
        return new DisplayContext(properties);
    }
}
