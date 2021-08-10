package edu.stanford.protege.webprotege.app;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.authorization.api.ActionId;
import edu.stanford.protege.webprotege.authorization.api.ApplicationResource;
import edu.stanford.protege.webprotege.user.UserDetails;
import edu.stanford.protege.webprotege.user.UserDetailsManager;
import edu.stanford.protege.webprotege.common.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.protege.webprotege.authorization.api.Subject.forUser;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 22 Dec 2017
 */
public class UserInSessionFactory {

    @Nonnull
    private final AccessManager accessManager;

    @Nonnull
    private final UserDetailsManager userDetailsManager;

    @Inject
    public UserInSessionFactory(@Nonnull AccessManager accessManager,
                                @Nonnull UserDetailsManager userDetailsManager) {
        this.accessManager = checkNotNull(accessManager);
        this.userDetailsManager = checkNotNull(userDetailsManager);
    }

    /**
     * Gets the user in session for the specified user id.
     * @param userId The user id.  This can be the id of the guest user.
     */
    @Nonnull
    public UserInSession getUserInSession(@Nonnull UserId userId) {
        UserDetails userDetails = userDetailsManager.getUserDetails(userId)
                                                    .orElse(UserDetails.getGuestUserDetails());
        Set<ActionId> actionClosure = accessManager.getActionClosure(forUser(userId),
                                                                     ApplicationResource.get());
        return new UserInSession(userDetails, actionClosure);
    }
}
