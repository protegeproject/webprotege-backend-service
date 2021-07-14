package edu.stanford.protege.webprotege.issues;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.entity.OWLEntityData;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Oct 2016
 */
@AutoValue

@JsonTypeName("GetEntityDiscussionThreads")
public abstract class GetEntityDiscussionThreadsResult implements Result {

    @JsonCreator
    public static GetEntityDiscussionThreadsResult create(@JsonProperty("entity") @Nonnull OWLEntityData entityData,
                                                          @JsonProperty("threads") @Nonnull ImmutableList<EntityDiscussionThread> threads) {
        return new AutoValue_GetEntityDiscussionThreadsResult(entityData, threads);
    }

    @Nonnull
    public abstract OWLEntityData getEntity();

    @Nonnull
    public abstract ImmutableList<EntityDiscussionThread> getThreads();
}
