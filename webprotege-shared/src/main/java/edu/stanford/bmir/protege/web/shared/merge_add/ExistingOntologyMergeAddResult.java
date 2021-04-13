package edu.stanford.bmir.protege.web.shared.merge_add;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("ExistingOntologyMergeAdd")
public class ExistingOntologyMergeAddResult implements Result {

    @JsonCreator
    public static ExistingOntologyMergeAddResult create() {
        return new AutoValue_ExistingOntologyMergeAddResult();
    }
}
