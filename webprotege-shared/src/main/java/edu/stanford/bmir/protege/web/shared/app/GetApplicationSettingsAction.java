package edu.stanford.bmir.protege.web.shared.app;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Mar 2017
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("GetApplicationSettings")
public abstract class GetApplicationSettingsAction implements Action<GetApplicationSettingsResult> {

    @JsonCreator
    public static GetApplicationSettingsAction create() {
        return new AutoValue_GetApplicationSettingsAction();
    }
}
