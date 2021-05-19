package edu.stanford.protege.webprotege.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-14
 */
@AutoValue
public abstract class Password {

    @JsonCreator
    @Nonnull
    public static Password create(@Nonnull String password) {
        return new AutoValue_Password(password);
    }

    @JsonValue
    @Nonnull
    public abstract String getPassword();

    @Override
    public String toString() {
        return "Password{}";
    }
}
