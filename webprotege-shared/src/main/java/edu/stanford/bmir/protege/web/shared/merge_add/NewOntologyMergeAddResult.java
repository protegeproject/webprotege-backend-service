package edu.stanford.bmir.protege.web.shared.merge_add;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;


@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("NewOntologyMergeAdd")
public abstract class NewOntologyMergeAddResult implements Result {

    @JsonCreator
    public static NewOntologyMergeAddResult create() {
        return new AutoValue_NewOntologyMergeAddResult();
    }
}
