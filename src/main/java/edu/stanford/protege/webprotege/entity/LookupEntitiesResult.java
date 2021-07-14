package edu.stanford.protege.webprotege.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.Result;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/11/2013
 */
@AutoValue

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
