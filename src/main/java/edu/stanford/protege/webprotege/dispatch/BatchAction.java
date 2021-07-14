package edu.stanford.protege.webprotege.dispatch;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Oct 2018
 */
@AutoValue

@JsonTypeName("Batch")
public abstract class BatchAction implements Action<BatchResult> {

    /**
     * Create an action that batches together the specified actions.
     * @param actions The actions.
     * @return The batch action.
     */
    @JsonCreator
    public static BatchAction create(@JsonProperty("actions") @Nonnull ImmutableList<Action<?>> actions) {
        return new AutoValue_BatchAction(actions);
    }


    @Nonnull
    public abstract ImmutableList<Action<?>> getActions();
}
