package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptorDto;

import javax.annotation.Nonnull;

@GwtCompatible(serializable = true)
@AutoValue
@JsonTypeName("SubFormControlDescriptorDto")
public abstract class SubFormControlDescriptorDto implements FormControlDescriptorDto {

    @JsonCreator
    @Nonnull
    public static SubFormControlDescriptorDto get(@JsonProperty("descriptor") @Nonnull FormDescriptorDto subformDescriptorDto) {
        return new AutoValue_SubFormControlDescriptorDto(subformDescriptorDto);
    }

    @Nonnull
    public abstract FormDescriptorDto getDescriptor();

    @Override
    public <R> R accept(FormControlDescriptorDtoVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public FormControlDescriptor toFormControlDescriptor() {
        return new SubFormControlDescriptor(getDescriptor().toFormDescriptor());
    }
}
