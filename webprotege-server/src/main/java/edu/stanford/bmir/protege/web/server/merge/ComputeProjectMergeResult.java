package edu.stanford.bmir.protege.web.server.merge;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;

import edu.stanford.bmir.protege.web.server.diff.DiffElement;
import edu.stanford.bmir.protege.web.server.dispatch.Result;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/01/15
 */
@AutoValue

@JsonTypeName("ComputeProjectMerge")
public abstract class ComputeProjectMergeResult implements Result {

    public abstract List<DiffElement<String, String>> getDiff();

    @JsonCreator
    public static ComputeProjectMergeResult create(@JsonProperty("diff") List<DiffElement<String, String>> diff) {
        return new AutoValue_ComputeProjectMergeResult(diff);
    }
}
