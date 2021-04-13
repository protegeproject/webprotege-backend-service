package edu.stanford.bmir.protege.web.shared.viz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-10
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("SetUserProjectEntityGraphSettings")
public abstract class SetUserProjectEntityGraphSettingsResult implements Result {

    @JsonCreator
    public static SetUserProjectEntityGraphSettingsResult create() {
        return new AutoValue_SetUserProjectEntityGraphSettingsResult();
    }
}
