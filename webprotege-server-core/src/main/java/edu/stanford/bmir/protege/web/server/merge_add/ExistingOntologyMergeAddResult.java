package edu.stanford.bmir.protege.web.server.merge_add;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;

import edu.stanford.bmir.protege.web.server.dispatch.Result;

@AutoValue

@JsonTypeName("ExistingOntologyMergeAdd")
public class ExistingOntologyMergeAddResult implements Result {

    @JsonCreator
    public static ExistingOntologyMergeAddResult create() {
        return new AutoValue_ExistingOntologyMergeAddResult();
    }
}
