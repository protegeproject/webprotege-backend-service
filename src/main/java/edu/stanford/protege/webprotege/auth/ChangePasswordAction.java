package edu.stanford.protege.webprotege.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.user.UserId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/02/15
 */
@AutoValue

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
