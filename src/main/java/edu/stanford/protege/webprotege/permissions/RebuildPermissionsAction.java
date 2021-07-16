package edu.stanford.protege.webprotege.permissions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.Action;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Apr 2017
 */
@AutoValue

@JsonTypeName("RebuildPermissions")
public abstract class RebuildPermissionsAction implements Action<RebuildPermissionsResult> {

    @JsonCreator
    public static RebuildPermissionsAction get() {
        return new AutoValue_RebuildPermissionsAction();
    }

    public static RebuildPermissionsAction create() {
        return get();
    }
}
