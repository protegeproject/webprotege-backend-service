package edu.stanford.bmir.protege.web.shared.mail;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.user.EmailAddress;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nullable;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/11/2013
 * <p>
 *     The result from a {@link GetEmailAddressAction}.
 * </p>
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("GetEmailAddress")
public abstract class GetEmailAddressResult implements Result {

    @JsonCreator
    public static GetEmailAddressResult create(@JsonProperty("userId") UserId userId,
                                               @JsonProperty("emailAddress") @Nullable EmailAddress emailAddress) {
        return new AutoValue_GetEmailAddressResult(userId, emailAddress);
    }

    /**
     * Gets the {@link UserId}.
     * @return The {@link UserId}. Not {@code null}.
     */
    public abstract UserId getUserId();

    @Nullable
    protected abstract EmailAddress getEmailAddressInternal();

    /**
     * Gets the {@link EmailAddress}.
     * @return The {@link EmailAddress}.  An absent value indicates that the email for the specified user id
     * does not exist.  Not {@code null}.
     */
    public Optional<EmailAddress> getEmailAddress() {
        return Optional.ofNullable(getEmailAddressInternal());
    }


}
