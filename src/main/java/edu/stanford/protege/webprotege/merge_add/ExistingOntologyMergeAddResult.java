package edu.stanford.protege.webprotege.merge_add;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.Result;

@AutoValue

@JsonTypeName("ExistingOntologyMergeAdd")
public class ExistingOntologyMergeAddResult implements Result {

    @JsonCreator
    public static ExistingOntologyMergeAddResult create() {
        return new AutoValue_ExistingOntologyMergeAddResult();
    }
}
