package edu.stanford.bmir.protege.web.shared.permissions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Apr 2017
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("RebuildPermissions")
public abstract class RebuildPermissionsAction implements Action<RebuildPermissionsResult> {

    @JsonCreator
    public static RebuildPermissionsAction get() {
        return new AutoValue_RebuildPermissionsAction();
    }
}
