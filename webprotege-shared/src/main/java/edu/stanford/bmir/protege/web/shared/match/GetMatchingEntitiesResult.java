package edu.stanford.bmir.protege.web.shared.match;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.pagination.Page;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Jun 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
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
