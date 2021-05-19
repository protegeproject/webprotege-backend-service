package edu.stanford.protege.webprotege.mail;

import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.user.UserId;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/11/2013
 */
@JsonTypeName("GetEmailAddress")
public class GetEmailAddressAction implements Action<GetEmailAddressResult> {

    private UserId userId;

    /**
     * For serialization purposes only
     */
    private GetEmailAddressAction() {
    }

    /**
     * Constructs a {@link GetEmailAddressAction} object to retrieve the email address of a particular user.
     * @param userId The {@link UserId} that identifies the user whose email address is to be retrieved.  Not {@code null}.
     * @throws NullPointerException if {@code userId} is {@code null}.
     */
    private GetEmailAddressAction(UserId userId) {
        this.userId = checkNotNull(userId);
        if(userId.isGuest()) {
            throw new IllegalArgumentException("userId must not be guest");
        }
    }

    public static GetEmailAddressAction create(UserId userId) {
        return new GetEmailAddressAction(userId);
    }

    /**
     * Gets the {@link UserId} of the user whose email address is to be retrieved.
     * @return The {@link UserId}.  Not {@code null}.
     */
    public UserId getUserId() {
        return userId;
    }

    @Override
    public int hashCode() {
        return "GetEmailAddressAction".hashCode() + userId.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof GetEmailAddressAction)) {
            return false;
        }
        GetEmailAddressAction other = (GetEmailAddressAction) o;
        return userId.equals(other.userId);
    }
}
