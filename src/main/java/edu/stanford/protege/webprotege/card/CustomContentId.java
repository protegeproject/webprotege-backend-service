package edu.stanford.protege.webprotege.card;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public record CustomContentId(String id) {

    @JsonCreator
    public static CustomContentId valueOf(String id) {
        return new CustomContentId(id);
    }

    @JsonValue
    public String id() {
        return id;
    }
}
