package edu.stanford.protege.webprotege.lang;

import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.shortform.DictionaryLanguage;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Aug 2018
 */
@AutoValue

public abstract class DictionaryLanguageUsage {

    public static DictionaryLanguageUsage get(@Nonnull DictionaryLanguage language,
                                              int referenceCount) {
        return new AutoValue_DictionaryLanguageUsage(language, referenceCount);
    }

    @Nonnull
    public abstract DictionaryLanguage getDictionaryLanguage();

    public abstract int getReferenceCount();
}
