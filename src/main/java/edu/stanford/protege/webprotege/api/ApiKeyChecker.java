package edu.stanford.protege.webprotege.api;

import edu.stanford.protege.webprotege.common.UserId;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Apr 2018
 */
@Deprecated
public interface ApiKeyChecker {

    /**
     * Gets the {@link UserId} that is associated with the specified API Key.
     * @param apiKey The API key.
     * @return The {@link UserId} that is associated with the specified API Key, or an empty value
     *         if there is no user associated with the specified API key.
     */
    Optional<UserId> getUserIdForApiKey(@Nonnull ApiKey apiKey);
}
