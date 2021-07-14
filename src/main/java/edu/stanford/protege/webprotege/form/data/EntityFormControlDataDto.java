package edu.stanford.protege.webprotege.form.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.entity.OWLEntityData;
import edu.stanford.protege.webprotege.entity.OWLPrimitiveData;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;
import java.util.Optional;

@AutoValue

@JsonTypeName("EntityFormControlDataDto")
public abstract class EntityFormControlDataDto extends PrimitiveFormControlDataDto {

    @JsonCreator
    public static EntityFormControlDataDto get(@JsonProperty("entity") @Nonnull OWLEntityData entityData) {
        return new AutoValue_EntityFormControlDataDto(entityData);
    }

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

    @Nonnull
    @Override
    public OWLPrimitiveData getPrimitiveData() {
        return getEntity();
    }
}
