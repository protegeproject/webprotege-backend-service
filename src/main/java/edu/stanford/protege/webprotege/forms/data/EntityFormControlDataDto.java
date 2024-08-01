package edu.stanford.protege.webprotege.forms.data;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.protege.webprotege.entity.OWLEntityData;
import edu.stanford.protege.webprotege.entity.OWLPrimitiveData;
import edu.stanford.protege.webprotege.forms.PropertyNames;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;
import java.util.Optional;

@AutoValue
@JsonTypeName("EntityFormControlDataDto")
public abstract class EntityFormControlDataDto extends PrimitiveFormControlDataDto {

    @JsonCreator
    public static EntityFormControlDataDto get(@JsonProperty(PropertyNames.ENTITY) @Nonnull OWLEntityData entityData) {
        return new AutoValue_EntityFormControlDataDto(entityData);
    }

    @JsonProperty(PropertyNames.ENTITY)
    @Nonnull
    public abstract OWLEntityData getEntity();

    @Nonnull
    @Override
    public PrimitiveFormControlData toPrimitiveFormControlData() {
        return EntityFormControlData.get(getEntity().getEntity());
    }

    @Nonnull
    @Override
    public Optional<OWLLiteral> asLiteral() {
        return Optional.empty();
    }

    @Override
    public boolean isDeprecated() {
        return getEntity().isDeprecated();
    }

    @JsonIgnore
    @Nonnull
    @Override
    public OWLPrimitiveData getPrimitiveData() {
        return getEntity();
    }
}
