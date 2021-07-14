package edu.stanford.protege.webprotege.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.Result;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/02/15
 */
@AutoValue

@JsonTypeName("ChangePassword")
public abstract class ChangePasswordResult implements Result {

    @Nonnull
    public abstract AuthenticationResponse getResponse();

    @JsonCreator
    public static ChangePasswordResult create(@JsonProperty("response") AuthenticationResponse response) {
        return new AutoValue_ChangePasswordResult(response);
    }
}
