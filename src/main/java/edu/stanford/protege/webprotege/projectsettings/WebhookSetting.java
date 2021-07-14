package edu.stanford.protege.webprotege.projectsettings;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.webhook.ProjectWebhookEventType;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Jun 2017
 */
@AutoValue

public abstract class WebhookSetting {

    public static final String PAYLOAD_URL = "payloadUrl";

    public static final String EVENT_TYPES = "eventTypes";

    @JsonProperty(PAYLOAD_URL)
    @Nonnull
    public abstract String getPayloadUrl();

    @JsonProperty(EVENT_TYPES)
    @Nonnull
    public abstract ImmutableSet<ProjectWebhookEventType> getEventTypes();

    @Nonnull
    public static WebhookSetting get(@Nonnull @JsonProperty(PAYLOAD_URL) String payloadUrl,
                                     @Nonnull @JsonProperty(EVENT_TYPES) Set<ProjectWebhookEventType> eventTypes) {
        return new AutoValue_WebhookSetting(payloadUrl, ImmutableSet.copyOf(eventTypes));
    }
}
