package edu.stanford.protege.webprotege.projectsettings;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Jun 2017
 */
@AutoValue

public abstract class WebhookSettings {

    @JsonProperty("webhookSettings")
    @Nonnull
    public abstract ImmutableList<WebhookSetting> getWebhookSettings();

    @Nonnull
    @JsonCreator
    public static WebhookSettings get(@Nonnull @JsonProperty("webhookSettings") List<WebhookSetting> webhookSettings) {
        return new AutoValue_WebhookSettings(ImmutableList.copyOf(webhookSettings));
    }
}
