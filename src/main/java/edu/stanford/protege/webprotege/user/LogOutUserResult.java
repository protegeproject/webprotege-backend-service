package edu.stanford.protege.webprotege.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.Objects;
import edu.stanford.protege.webprotege.app.UserInSession;
import edu.stanford.protege.webprotege.dispatch.Result;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/02/15
 */
@JsonTypeName("LogOutUser")
public class LogOutUserResult implements Result {

    private UserInSession userInSession;

    @JsonCreator
    public LogOutUserResult(@JsonProperty("userInSession") @Nonnull UserInSession userInSession) {
        this.userInSession = checkNotNull(userInSession);
    }


    private LogOutUserResult() {
    }

    @Nonnull
    public UserInSession getUserInSession() {
        return userInSession;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userInSession);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof LogOutUserResult)) {
            return false;
        }
        LogOutUserResult other = (LogOutUserResult) obj;
        return this.userInSession.equals(other.userInSession);
    }


    @Override
    public String toString() {
        return toStringHelper("LogOutUserResult")
                .toString();
    }
}
