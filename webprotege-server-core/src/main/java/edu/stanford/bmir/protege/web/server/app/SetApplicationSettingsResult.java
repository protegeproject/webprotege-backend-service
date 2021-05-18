package edu.stanford.bmir.protege.web.server.app;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;

import edu.stanford.bmir.protege.web.server.dispatch.Result;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Mar 2017
 */
@AutoValue

@JsonTypeName("SetApplicationSettings")
public abstract class SetApplicationSettingsResult implements Result {

    @JsonCreator
    public static SetApplicationSettingsResult create() {
        return new AutoValue_SetApplicationSettingsResult();
    }
}
