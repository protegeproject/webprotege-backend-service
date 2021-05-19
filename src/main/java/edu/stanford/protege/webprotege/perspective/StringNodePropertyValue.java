package edu.stanford.protege.webprotege.perspective;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-20
 */
@AutoValue
@JsonTypeName("String")
public abstract class StringNodePropertyValue implements NodePropertyValue {

    private static final String VALUE = "value";

    @Nonnull
    @JsonCreator
    public static StringNodePropertyValue get(@Nonnull @JsonProperty(VALUE) String value) {
        return new AutoValue_StringNodePropertyValue(value);
    }

    @JsonProperty(VALUE)
    public abstract String getValue();
}

