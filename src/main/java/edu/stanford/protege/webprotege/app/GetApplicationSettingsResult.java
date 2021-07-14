package edu.stanford.protege.webprotege.app;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.Result;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Mar 2017
 */
@AutoValue

@JsonTypeName("GetApplicationSettings")
public abstract class GetApplicationSettingsResult implements Result {

    @JsonCreator
    public static GetApplicationSettingsResult create(@JsonProperty("applicationSettings") ApplicationSettings applicationSettings) {
        return new AutoValue_GetApplicationSettingsResult(applicationSettings);
    }

    public abstract ApplicationSettings getApplicationSettings();
}
