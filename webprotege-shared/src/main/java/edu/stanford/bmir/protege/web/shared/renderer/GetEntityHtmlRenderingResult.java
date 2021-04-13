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
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-03-27
 */
@AutoValue
@GwtCompatible(serializable = true)
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
