package edu.stanford.protege.webprotege.user;

import javax.annotation.Nonnull;

/**
 * Signals that a user lookup (by user id or email address) could not be completed due to an
 * infrastructure failure - for example a timeout, or an RPC/messaging error communicating with
 * the remote user-query service.  This is distinct from {@link java.util.Optional#empty()}, which
 * signals that the lookup completed successfully and confirmed that no such user exists.
 */
public class UserLookupException extends RuntimeException {

    public UserLookupException(@Nonnull String message, @Nonnull Throwable cause) {
        super(message, cause);
    }
}
