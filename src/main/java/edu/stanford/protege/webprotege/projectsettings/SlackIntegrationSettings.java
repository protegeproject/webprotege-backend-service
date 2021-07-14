package edu.stanford.protege.webprotege.projectsettings;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Jun 2017
 */
@AutoValue

public abstract class SlackIntegrationSettings {

    public static final String PAYLOAD_URL = "payloadUrl";

    @JsonProperty(PAYLOAD_URL)
    @Nonnull
    public abstract String getPayloadUrl();

    @JsonCreator
    @Nonnull
    public static SlackIntegrationSettings get(@JsonProperty(PAYLOAD_URL) @Nonnull String payloadUrl) {
        return new AutoValue_SlackIntegrationSettings(payloadUrl);
    }
}
