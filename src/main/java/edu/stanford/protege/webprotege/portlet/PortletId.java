package edu.stanford.protege.webprotege.portlet;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public record PortletId(String value) {

    @JsonCreator
    public static PortletId valueOf(String value) {
        return new PortletId(value);
    }

    @JsonValue
    public String value() {
        return this.value;
    }
}
