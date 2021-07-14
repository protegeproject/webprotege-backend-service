package edu.stanford.protege.webprotege.renderer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.entity.OWLEntityData;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-03-27
 */
@AutoValue

@JsonTypeName("GetEntityHtmlRendering")
public abstract class GetEntityHtmlRenderingResult implements Result {

    @JsonCreator
    public static GetEntityHtmlRenderingResult create(@JsonProperty("entityData") OWLEntityData entityData,
                                                      @JsonProperty("rendering") @Nonnull String rendering) {
        return new AutoValue_GetEntityHtmlRenderingResult(entityData, rendering);
    }

    @Nonnull
    public abstract OWLEntityData getEntityData();

    @Nonnull
    public abstract String getRendering();
}
