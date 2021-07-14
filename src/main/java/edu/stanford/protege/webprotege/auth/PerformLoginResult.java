package edu.stanford.protege.webprotege.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.app.UserInSession;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.user.UserDetails;

import javax.annotation.Nonnull;

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
