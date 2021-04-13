package edu.stanford.bmir.protege.web.shared.sharing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 07/02/15
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("SetProjectSharingSettings")
public abstract class SetProjectSharingSettingsResult implements Result {

    @JsonCreator
    public static SetProjectSharingSettingsResult create() {
        return new AutoValue_SetProjectSharingSettingsResult();
    }
}
