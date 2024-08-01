package edu.stanford.protege.webprotege.forms.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.protege.webprotege.entity.OWLEntityData;
import edu.stanford.protege.webprotege.forms.PropertyNames;
import edu.stanford.protege.webprotege.forms.field.EntityNameControlDescriptor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

@AutoValue
@JsonTypeName("EntityNameControlDataDto")
public abstract class EntityNameControlDataDto implements FormControlDataDto {

    @JsonCreator
    public static EntityNameControlDataDto get(@JsonProperty(PropertyNames.CONTROL) @Nonnull EntityNameControlDescriptor descriptor,
                                               @JsonProperty(PropertyNames.ENTITY) @Nullable OWLEntityData entityData,
                                               @JsonProperty(PropertyNames.DEPTH) int depth) {
        return new AutoValue_EntityNameControlDataDto(depth, descriptor, entityData);
    }

    @JsonProperty(PropertyNames.CONTROL)
    @Nonnull
    public abstract EntityNameControlDescriptor getDescriptor();

    @Nullable
    @JsonProperty(PropertyNames.VALUE)
    protected abstract OWLEntityData getEntityInternal();

    @JsonIgnore
    @Nonnull
    public Optional<OWLEntityData> getEntity() {
        return Optional.ofNullable(getEntityInternal());
    }

    @Override
    public <R> R accept(FormControlDataDtoVisitorEx<R> visitor) {
        return visitor.visit(this);
    }

    @Nonnull
    @Override
    public FormControlData toFormControlData() {
        return EntityNameControlData.get(getDescriptor(), getEntity().map(OWLEntityData::getEntity).orElse(null));
    }
}
