package edu.stanford.protege.webprotege.match;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.entity.EntityNode;
import edu.stanford.protege.webprotege.pagination.Page;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Jun 2018
 */
@AutoValue

@JsonTypeName("GetMatchingEntitiesResult")
public abstract class GetMatchingEntitiesResult implements Result {

    @Nonnull
    public abstract Page<EntityNode> getEntities();

    @JsonCreator
    @Nonnull
    public static GetMatchingEntitiesResult create(@JsonProperty("entities") @Nonnull Page<EntityNode> entities) {
        return new AutoValue_GetMatchingEntitiesResult(entities);
    }
}
