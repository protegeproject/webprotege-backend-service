package edu.stanford.bmir.protege.web.shared.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.pagination.Page;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Jun 2017
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("GetDeprecatedEntities")
public abstract class GetDeprecatedEntitiesResult implements Result {

    @JsonCreator
    public static GetDeprecatedEntitiesResult create(@JsonProperty("entities") @Nonnull Page<OWLEntityData> entities) {
        return new AutoValue_GetDeprecatedEntitiesResult(entities);
    }

    @Nonnull
    public abstract Page<OWLEntityData> getEntities();
}
