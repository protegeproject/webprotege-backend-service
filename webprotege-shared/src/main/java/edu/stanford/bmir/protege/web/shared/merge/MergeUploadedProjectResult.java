package edu.stanford.bmir.protege.web.shared.merge;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/01/15
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("MergeUploadedProject")
public abstract class MergeUploadedProjectResult implements Result {

    @JsonCreator
    public static MergeUploadedProjectResult create() {
        return new AutoValue_MergeUploadedProjectResult();
    }
}
