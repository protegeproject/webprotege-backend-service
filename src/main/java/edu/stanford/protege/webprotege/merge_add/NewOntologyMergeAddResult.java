package edu.stanford.protege.webprotege.merge_add;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.Result;


@AutoValue

@JsonTypeName("NewOntologyMergeAdd")
public abstract class NewOntologyMergeAddResult implements Result {

    @JsonCreator
    public static NewOntologyMergeAddResult create() {
        return new AutoValue_NewOntologyMergeAddResult();
    }
}
