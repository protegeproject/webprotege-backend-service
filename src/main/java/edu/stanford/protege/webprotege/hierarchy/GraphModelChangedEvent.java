package edu.stanford.protege.webprotege.hierarchy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-20
 */
@AutoValue
public abstract class GraphModelChangedEvent<U> {

    public abstract ImmutableList<GraphModelChange<U>> getChanges();

    @JsonCreator
    public static <U> GraphModelChangedEvent<U> create(@JsonProperty("changes") ImmutableList<GraphModelChange<U>> changes) {
        return new AutoValue_GraphModelChangedEvent<>(changes);
    }

}
