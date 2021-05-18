package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;


import edu.stanford.bmir.protege.web.shared.form.data.PrimitiveFormControlData;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */

@AutoValue
public abstract class ChoiceDescriptor {

    @JsonCreator
    public static ChoiceDescriptor choice(@Nonnull @JsonProperty("label") LanguageMap label,
                                          @Nonnull @JsonProperty("value") PrimitiveFormControlData value) {
        return new AutoValue_ChoiceDescriptor(label, value);
    }

    @Nonnull
    public abstract LanguageMap getLabel();

    @Nonnull
    public abstract PrimitiveFormControlData getValue();
}