package edu.stanford.protege.webprotege.forms.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.forms.*;

import javax.annotation.Nonnull;


@AutoValue
@JsonTypeName("SubFormControlDescriptorDto")
public abstract class SubFormControlDescriptorDto implements FormControlDescriptorDto {

    @JsonCreator
    @Nonnull
    public static SubFormControlDescriptorDto get(@JsonProperty(PropertyNames.FORM) @Nonnull FormDescriptorDto subformDescriptorDto) {
        return new AutoValue_SubFormControlDescriptorDto(subformDescriptorDto);
    }

    @Nonnull
    @JsonProperty(PropertyNames.FORM)
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
