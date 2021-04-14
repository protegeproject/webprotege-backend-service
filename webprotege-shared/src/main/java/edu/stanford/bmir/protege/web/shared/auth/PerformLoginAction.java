package edu.stanford.bmir.protege.web.shared.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14/02/15
 */
@AutoValue
@GwtCompatible(serializable = true)
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

