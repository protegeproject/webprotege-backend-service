package edu.stanford.protege.webprotege.auth;

import edu.stanford.protege.webprotege.user.EmailAddress;
import edu.stanford.protege.webprotege.user.UserDetails;
import edu.stanford.protege.webprotege.user.UserId;
import edu.stanford.protege.webprotege.user.UserRegistrationException;

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
