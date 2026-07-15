package edu.stanford.protege.webprotege.user;

import edu.stanford.protege.webprotege.common.UserId;

import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public interface UserDetailsManager {

    List<UserId> getUserIdsContainingIgnoreCase(String userName, int limit);

    /**
     * Gets the {@link UserDetails} for the specified UserId.
     * @param userId The UserId of the user whose details are to be retrieved.
     * @return If the userId is the guest user then the guest user details will be returned, otherwise,
     * the details of the specified userId.  If the userId does not exists then an empty value will
     * be returned.
     */
    Optional<UserDetails> getUserDetails(UserId userId);

    Optional<String> getEmail(UserId userId);

    void setEmail(UserId userId, String email);

    /**
     * Retrieves a UserId by their email address.
     * @param emailAddress The email address.  Not {@code null}.
     * @return The UserId that has the specified email address.  An absent value will be returned if there is
     * no user with the specified email address.
     */
    Optional<UserId> getUserIdByEmailAddress(EmailAddress emailAddress);

    /**
     * Gets a User by its user id or it's email address.
     * @param userNameOrEmail The user id or email address as a string.  Not {@code null}.
     * @return The User.  An absent value will be returned only if the lookup completed successfully
     * and confirmed that there is no such user with the specified id or email address - it never
     * means "the lookup could not be checked". Not {@code null}.
     * @throws UserLookupException if the lookup could not be completed, for example due to a timeout
     * or an RPC/messaging failure communicating with the remote user-query service.
     * @throws RuntimeException if the specified id or email address matches more than one user - this
     * indicates a data-integrity problem rather than an infrastructure failure or a confirmed absence.
     */
    Optional<UserId> getUserByUserIdOrEmail(String userNameOrEmail);


}
