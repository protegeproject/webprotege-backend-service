package edu.stanford.protege.webprotege.viz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-11
 */
@AutoValue

public abstract class FilterName {

    @Nonnull
    @JsonCreator
    public static FilterName get(@Nonnull String name) {
        return new AutoValue_FilterName(name);
    }

    @JsonValue
    @Nonnull
    public abstract String getName();
}
