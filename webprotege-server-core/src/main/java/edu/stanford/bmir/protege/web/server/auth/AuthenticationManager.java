package edu.stanford.bmir.protege.web.server.auth;

import edu.stanford.bmir.protege.web.shared.auth.AuthenticationResponse;
import edu.stanford.bmir.protege.web.shared.auth.Password;
import edu.stanford.bmir.protege.web.shared.user.EmailAddress;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.user.UserRegistrationException;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public interface AuthenticationManager {

    UserDetails registerUser(UserId userId, EmailAddress email, Password password) throws UserRegistrationException;

    AuthenticationResponse authenticateUser(@Nonnull UserId userId,
                                            @Nonnull Password password);

    void setPassword(@Nonnull UserId userId,
                     @Nonnull Password newPassword);
}
