package edu.stanford.bmir.protege.web.server.projectsettings;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;

import edu.stanford.bmir.protege.web.server.dispatch.Result;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25/11/14
 */
@AutoValue

@JsonTypeName("GetProjectSettings")
public abstract class GetProjectSettingsResult implements Result {

    @JsonCreator
    public static GetProjectSettingsResult create(@JsonProperty("settings") @Nonnull ProjectSettings settings) {
        return new AutoValue_GetProjectSettingsResult(settings);
    }

    /**
     * Gets the {@link ProjectSettings}.
     * @return The project settings.  Not {@code null}.
     */
    public abstract ProjectSettings getSettings();
}

