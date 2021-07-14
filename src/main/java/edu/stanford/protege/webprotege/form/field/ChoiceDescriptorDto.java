package edu.stanford.protege.webprotege.form.field;

import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.form.data.PrimitiveFormControlDataDto;
import edu.stanford.protege.webprotege.lang.LanguageMap;

import javax.annotation.Nonnull;

@AutoValue

public abstract class ChoiceDescriptorDto {

    @Nonnull
    public static ChoiceDescriptorDto get(@Nonnull PrimitiveFormControlDataDto value,
                                          @Nonnull LanguageMap label) {
        return new AutoValue_ChoiceDescriptorDto(label, value);
    }

    @Nonnull
    public abstract LanguageMap getLabel();

    @Nonnull
    public abstract PrimitiveFormControlDataDto getValue();
}
