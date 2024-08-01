package edu.stanford.protege.webprotege.forms.field;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.common.LanguageMap;
import edu.stanford.protege.webprotege.forms.PropertyNames;
import edu.stanford.protege.webprotege.forms.data.PrimitiveFormControlDataDto;

import javax.annotation.Nonnull;

@AutoValue

public abstract class ChoiceDescriptorDto {

    @JsonCreator
    @Nonnull
    public static ChoiceDescriptorDto get(@JsonProperty(PropertyNames.VALUE) @Nonnull PrimitiveFormControlDataDto value,
                                          @JsonProperty(PropertyNames.LABEL) @Nonnull LanguageMap label) {
        return new AutoValue_ChoiceDescriptorDto(label, value);
    }

    @JsonProperty(PropertyNames.LABEL)
    @Nonnull
    public abstract LanguageMap getLabel();

    @JsonProperty(PropertyNames.VALUE)
    @Nonnull
    public abstract PrimitiveFormControlDataDto getValue();
}
