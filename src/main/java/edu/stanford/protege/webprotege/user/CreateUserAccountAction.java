package edu.stanford.protege.webprotege.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.auth.Password;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.dispatch.Action;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/02/15
 */
@AutoValue
@Deprecated
@JsonTypeName("CreateUserAccount")
public abstract class CreateUserAccountAction implements Action<CreateUserAccountResult> {

    public static final String CHANNEL = "accounts.CreateUserAccount";

    @JsonCreator
    public static CreateUserAccountAction create(@JsonProperty("userId") UserId userId,
                                                 @JsonProperty("emailAddress") EmailAddress emailAddress,
                                                 @JsonProperty("password") Password password) {
        return new AutoValue_CreateUserAccountAction(userId, emailAddress, password);
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    public abstract UserId getUserId();

    public abstract EmailAddress getEmailAddress();

    public abstract Password getPassword();
}

