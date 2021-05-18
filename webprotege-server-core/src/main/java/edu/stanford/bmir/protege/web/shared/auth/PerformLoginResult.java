package edu.stanford.bmir.protege.web.shared.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;

import com.google.common.base.Objects;

import edu.stanford.bmir.protege.web.shared.app.UserInSession;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14/02/15
 */
@AutoValue

@JsonTypeName("PerformLogin")
public abstract class PerformLoginResult implements Result {

    @JsonCreator
    public static PerformLoginResult create(@JsonProperty("authenticationResponse") @Nonnull AuthenticationResponse authenticationResponse,
                                            @JsonProperty("userInSession") @Nonnull UserInSession userInSession) {
        return new AutoValue_PerformLoginResult(authenticationResponse, userInSession);
    }

    /**
     * Gets the user details of the user after the attempted login.  If authentication failed then the guest
     * details will be returned.
     */
    @JsonIgnore
    @Nonnull
    public UserDetails getUserDetails() {
        return getUserInSession().getUserDetails();
    }

    @Nonnull
    public abstract AuthenticationResponse getAuthenticationResponse();

    @Nonnull
    public abstract UserInSession getUserInSession();
}
