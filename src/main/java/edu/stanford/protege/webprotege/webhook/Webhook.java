package edu.stanford.protege.webprotege.webhook;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Feb 2018
 */
public interface Webhook {

    /**
     * Get the payload Url for the webhook
     * @return The payload Url
     */
    @Nonnull
    String getPayloadUrl();
}
