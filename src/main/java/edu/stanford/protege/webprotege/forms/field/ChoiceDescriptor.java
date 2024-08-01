package edu.stanford.protege.webprotege.forms.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.common.LanguageMap;
import edu.stanford.protege.webprotege.forms.PropertyNames;
import edu.stanford.protege.webprotege.forms.data.PrimitiveFormControlData;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */

@AutoValue
public abstract class ChoiceDescriptor {

    @JsonCreator
    public static ChoiceDescriptor choice(@Nonnull @JsonProperty(PropertyNames.LABEL) LanguageMap label,
                                          @Nonnull @JsonProperty(PropertyNames.VALUE) PrimitiveFormControlData value) {
        return new AutoValue_ChoiceDescriptor(label, value);
    }

    @JsonProperty(PropertyNames.LABEL)
    @Nonnull
    public abstract LanguageMap getLabel();

    @JsonProperty(PropertyNames.VALUE)
    @Nonnull
    public abstract PrimitiveFormControlData getValue();
}
