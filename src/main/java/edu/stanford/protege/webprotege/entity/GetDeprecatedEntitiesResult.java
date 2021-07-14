package edu.stanford.protege.webprotege.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.pagination.Page;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Jun 2017
 */
@AutoValue

@JsonTypeName("GetDeprecatedEntities")
public abstract class GetDeprecatedEntitiesResult implements Result {

    @JsonCreator
    public static GetDeprecatedEntitiesResult create(@JsonProperty("entities") @Nonnull Page<OWLEntityData> entities) {
        return new AutoValue_GetDeprecatedEntitiesResult(entities);
    }

    @Nonnull
    public abstract Page<OWLEntityData> getEntities();
}
