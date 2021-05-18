package edu.stanford.bmir.protege.web.shared.form.field;

import com.google.auto.value.AutoValue;

import edu.stanford.bmir.protege.web.shared.form.data.PrimitiveFormControlDataDto;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

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