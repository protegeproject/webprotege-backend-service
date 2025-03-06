package edu.stanford.protege.webprotege.card;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public record CardId(String value) {

    @JsonCreator
    public static CardId valueOf(String value) {
        return new CardId(value);
    }

    @JsonValue
    public String value() {
        return this.value;
    }
}
