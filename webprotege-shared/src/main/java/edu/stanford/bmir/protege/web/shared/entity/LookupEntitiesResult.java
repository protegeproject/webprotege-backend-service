package edu.stanford.bmir.protege.web.shared.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/11/2013
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("LookupEntities")
public abstract class LookupEntitiesResult implements Result {

    /**
     * Creates a LookupEntitiesResult.
     * @param entityLookupResult The match result.  Not {@code null}.
     * @throws NullPointerException if {@code entityLookupResult} is {@code null}.
     */
    @JsonCreator
    public static LookupEntitiesResult create(@JsonProperty("entityLookupResults") List<EntityLookupResult> entityLookupResult) {
        return new AutoValue_LookupEntitiesResult(entityLookupResult);
    }

    /**
     * Gets the entity matches.
     * @return The EntityLookupResult that describes the matched entities.  Not {@code null}.
     */
    public abstract List<EntityLookupResult> getEntityLookupResults();
}
