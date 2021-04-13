package edu.stanford.bmir.protege.web.shared.dispatch;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Oct 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("Batch")
public abstract class BatchResult implements Result {

    @JsonCreator
    @Nonnull
    public static BatchResult get(@JsonProperty("results") @Nonnull ImmutableList<ActionExecutionResult> results) {
        return new AutoValue_BatchResult(results);
    }

    @Nonnull
    public abstract ImmutableList<ActionExecutionResult> getResults();
}
