package edu.stanford.protege.webprotege.forms.data;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.common.Page;
import edu.stanford.protege.webprotege.forms.PropertyNames;
import edu.stanford.protege.webprotege.forms.field.FormFieldDescriptor;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-06
 */
@AutoValue

public abstract class FormFieldData {

    @JsonCreator
    public static FormFieldData get(@JsonProperty(PropertyNames.FIELD) @Nonnull FormFieldDescriptor descriptor,
                                    @JsonProperty(PropertyNames.DATA) @Nonnull Page<FormControlData> formControlData) {
        return new AutoValue_FormFieldData(descriptor, formControlData);
    }

    @Nonnull
    @JsonProperty(PropertyNames.FIELD)
    public abstract FormFieldDescriptor getFormFieldDescriptor();

    /**
     * Gets the page of form control values for this field.
     */
    @Nonnull
    @JsonProperty(PropertyNames.DATA)
    public abstract Page<FormControlData> getFormControlData();

    @JsonIgnore
    public boolean isEmpty() {
        return getFormControlData().getPageElements().isEmpty();
    }

    @JsonIgnore
    public boolean isNonEmpty() {
        return !isEmpty();
    }
}
