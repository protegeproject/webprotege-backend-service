package edu.stanford.bmir.protege.web.shared.permissions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Apr 2017
 */
@AutoValue

@JsonTypeName("RebuildPermissions")
public class RebuildPermissionsResult implements Result {

    @JsonCreator
    public static RebuildPermissionsResult get() {
        return new AutoValue_RebuildPermissionsResult();
    }
}
