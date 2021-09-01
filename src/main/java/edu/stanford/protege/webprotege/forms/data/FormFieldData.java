package edu.stanford.protege.webprotege.forms.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.forms.field.FormFieldDescriptor;
import edu.stanford.protege.webprotege.common.Page;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-06
 */
@AutoValue

public abstract class FormFieldData {

    @JsonCreator
    public static FormFieldData get(@JsonProperty("descriptor") @Nonnull FormFieldDescriptor descriptor,
                                    @JsonProperty("formControlData") @Nonnull Page<FormControlData> formControlData) {
        return new AutoValue_FormFieldData(descriptor, formControlData);
    }

    @Nonnull
    public abstract FormFieldDescriptor getFormFieldDescriptor();

    /**
     * Gets the page of form control values for this field.
     */
    @Nonnull
    public abstract Page<FormControlData> getFormControlData();

    public boolean isEmpty() {
        return getFormControlData().getPageElements().isEmpty();
    }

    public boolean isNonEmpty() {
        return !isEmpty();
    }
}
