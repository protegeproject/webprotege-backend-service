package edu.stanford.protege.webprotege.app;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.Action;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Mar 2017
 */
@AutoValue

@JsonTypeName("GetApplicationSettings")
public abstract class GetApplicationSettingsAction implements Action<GetApplicationSettingsResult> {

    @JsonCreator
    public static GetApplicationSettingsAction create() {
        return new AutoValue_GetApplicationSettingsAction();
    }
}
