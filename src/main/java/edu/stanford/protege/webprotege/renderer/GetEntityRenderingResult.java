package edu.stanford.protege.webprotege.renderer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.entity.OWLEntityData;

import javax.annotation.Nonnull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/02/2014
 */
@AutoValue

@JsonTypeName("GetEntityRendering")
public abstract class GetEntityRenderingResult implements Result {

    @JsonCreator
    public static GetEntityRenderingResult create(@JsonProperty("entityData") @Nonnull OWLEntityData entityData) {
        return new AutoValue_GetEntityRenderingResult(entityData);
    }

    @Nonnull
    public abstract OWLEntityData getEntityData();
}
