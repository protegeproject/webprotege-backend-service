package edu.stanford.bmir.protege.web.shared.app;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Mar 2017
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("SetApplicationSettings")
public abstract class SetApplicationSettingsResult implements Result {

    @JsonCreator
    public static SetApplicationSettingsResult create() {
        return new AutoValue_SetApplicationSettingsResult();
    }
}
