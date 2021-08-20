package edu.stanford.protege.webprotege.merge_add;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.Result;


@AutoValue

@JsonTypeName("MergeOntologies")
public abstract class MergeOntologiesResult implements Result {

    @JsonCreator
    public static MergeOntologiesResult create() {
        return new AutoValue_MergeOntologiesResult();
    }
}
