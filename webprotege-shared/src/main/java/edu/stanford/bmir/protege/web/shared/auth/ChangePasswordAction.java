package edu.stanford.bmir.protege.web.shared.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/02/15
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("ChangePassword")
public abstract class ChangePasswordAction implements Action<ChangePasswordResult> {

    @JsonCreator
    public static ChangePasswordAction create(@JsonProperty("userId") UserId userId,
                                              @JsonProperty("currentPassword") Password currentPassword,
                                              @JsonProperty("newPassword") Password newPassword) {
        return new AutoValue_ChangePasswordAction(userId, currentPassword, newPassword);
    }

    public abstract UserId getUserId();

    public abstract Password getCurrentPassword();

    public abstract Password getNewPassword();
}
