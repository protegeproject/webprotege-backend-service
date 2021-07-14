package edu.stanford.protege.webprotege.search;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.Result;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-17
 */
@AutoValue

@JsonTypeName("SetSearchSettings")
public class SetSearchSettingsResult implements Result {

    @JsonCreator
    public static SetSearchSettingsResult create() {
        return new AutoValue_SetSearchSettingsResult();
    }
}
