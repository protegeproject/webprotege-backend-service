package edu.stanford.protege.webprotege.forms.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.forms.field.FormFieldDescriptorDto;
import edu.stanford.protege.webprotege.common.Page;

import javax.annotation.Nonnull;

@AutoValue

public abstract class FormFieldDataDto {

    @JsonCreator
    @Nonnull
    public static FormFieldDataDto get(@JsonProperty("formFieldDescriptor") @Nonnull FormFieldDescriptorDto descriptor,
                                       @JsonProperty("formControlData") @Nonnull Page<FormControlDataDto> formControlData) {
        return new AutoValue_FormFieldDataDto(descriptor, formControlData);
    }

    @Nonnull
    public abstract FormFieldDescriptorDto getFormFieldDescriptor();

    /**
     * Gets the page of form control values for this field.
     */
    @Nonnull
    public abstract Page<FormControlDataDto> getFormControlData();

    @Nonnull
    public FormFieldData toFormFieldData() {
        return FormFieldData.get(
                getFormFieldDescriptor().toFormFieldDescriptor(),
                getFormControlData().transform(FormControlDataDto::toFormControlData));
    }

    @JsonIgnore
    @Nonnull
    public FormFieldData getFormFieldData() {
        return FormFieldData.get(getFormFieldDescriptor().toFormFieldDescriptor(),
                getFormControlData().transform(FormControlDataDto::toFormControlData));
    }
}
