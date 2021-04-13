package edu.stanford.bmir.protege.web.shared.app;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Mar 2017
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("GetApplicationSettings")
public abstract class GetApplicationSettingsResult implements Result {

    @JsonCreator
    public static GetApplicationSettingsResult create(@JsonProperty("applicationSettings") ApplicationSettings applicationSettings) {
        return new AutoValue_GetApplicationSettingsResult(applicationSettings);
    }

    public abstract ApplicationSettings getApplicationSettings();
}
