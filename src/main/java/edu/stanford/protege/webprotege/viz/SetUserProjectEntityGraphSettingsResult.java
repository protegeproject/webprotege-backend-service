package edu.stanford.protege.webprotege.viz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.Result;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-10
 */
@AutoValue

@JsonTypeName("SetUserProjectEntityGraphSettings")
public abstract class SetUserProjectEntityGraphSettingsResult implements Result {

    @JsonCreator
    public static SetUserProjectEntityGraphSettingsResult create() {
        return new AutoValue_SetUserProjectEntityGraphSettingsResult();
    }
}
