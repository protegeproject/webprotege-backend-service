package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nonnull;

@GwtCompatible(serializable = true)
@AutoValue
@JsonTypeName("TextControlDescriptorDto")
public abstract class TextControlDescriptorDto implements FormControlDescriptorDto {

    @JsonCreator
    public static TextControlDescriptorDto get(@JsonProperty("descriptor") @Nonnull TextControlDescriptor descriptor) {
        return new AutoValue_TextControlDescriptorDto(descriptor);
    }

    @Nonnull
    public abstract TextControlDescriptor getDescriptor();

    @Override
    public <R> R accept(FormControlDescriptorDtoVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public FormControlDescriptor toFormControlDescriptor() {
        return getDescriptor();
    }
}
