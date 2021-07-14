package edu.stanford.protege.webprotege.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.user.UserId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14/02/15
 */
@AutoValue

@JsonTypeName("PerformLogin")
public abstract class PerformLoginAction implements Action<PerformLoginResult> {


    @JsonCreator
    public static PerformLoginAction create(@JsonProperty("userId") UserId userId,
                                            @JsonProperty("password") Password password) {
        return new AutoValue_PerformLoginAction(userId, password);
    }

    @Nonnull
    public abstract UserId getUserId();

    @Nonnull
    public abstract Password getPassword();
}

