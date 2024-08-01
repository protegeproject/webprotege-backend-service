package edu.stanford.protege.webprotege.forms.field;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.common.LanguageMap;
import edu.stanford.protege.webprotege.forms.PropertyNames;

import javax.annotation.Nonnull;


@AutoValue
@JsonTypeName("NumberControlDescriptorDto")
public abstract class NumberControlDescriptorDto implements FormControlDescriptorDto {

    @JsonCreator
    @Nonnull
    public static NumberControlDescriptorDto get(@JsonProperty(PropertyNames.CONTROL) @Nonnull NumberControlDescriptor descriptor) {
        return new AutoValue_NumberControlDescriptorDto(descriptor);
    }

    @Nonnull
    @JsonProperty(PropertyNames.CONTROL)
    public abstract NumberControlDescriptor getDescriptor();

    @Override
    public <R> R accept(FormControlDescriptorDtoVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public FormControlDescriptor toFormControlDescriptor() {
        return getDescriptor();
    }

    @JsonIgnore
    public String getFormat() {
        return getDescriptor().getFormat();
    }

    @JsonIgnore
    public NumberControlRange getRange() {
        return getDescriptor().getRange();
    }

    @JsonIgnore
    public LanguageMap getPlaceholder() {
        return getDescriptor().getPlaceholder();
    }

    @JsonIgnore
    public int getLength() {
        return getDescriptor().getLength();
    }
}
