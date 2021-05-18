package edu.stanford.bmir.protege.web.shared.search;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;

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