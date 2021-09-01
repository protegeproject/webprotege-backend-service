package edu.stanford.protege.webprotege.forms.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.common.LanguageMap;

import javax.annotation.Nonnull;


@AutoValue
@JsonTypeName("NumberControlDescriptorDto")
public abstract class NumberControlDescriptorDto implements FormControlDescriptorDto {

    @JsonCreator
    @Nonnull
    public static NumberControlDescriptorDto get(@JsonProperty("descriptor") @Nonnull NumberControlDescriptor descriptor) {
        return new AutoValue_NumberControlDescriptorDto(descriptor);
    }

    @Nonnull
    public abstract NumberControlDescriptor getDescriptor();

    @Override
    public <R> R accept(FormControlDescriptorDtoVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public FormControlDescriptor toFormControlDescriptor() {
        return getDescriptor();
    }

    public String getFormat() {
        return getDescriptor().getFormat();
    }

    public NumberControlRange getRange() {
        return getDescriptor().getRange();
    }

    public LanguageMap getPlaceholder() {
        return getDescriptor().getPlaceholder();
    }

    public int getLength() {
        return getDescriptor().getLength();
    }
}
