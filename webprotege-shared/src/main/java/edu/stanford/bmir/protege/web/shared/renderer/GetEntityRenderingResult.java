package edu.stanford.bmir.protege.web.shared.renderer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/02/2014
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("GetEntityRendering")
public abstract class GetEntityRenderingResult implements Result {

    @JsonCreator
    public static GetEntityRenderingResult create(@JsonProperty("entityData") @Nonnull OWLEntityData entityData) {
        return new AutoValue_GetEntityRenderingResult(entityData);
    }

    @Nonnull
    public abstract OWLEntityData getEntityData();
}
