package edu.stanford.bmir.protege.web.shared.lang;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-26
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class LangTag {

    @JsonCreator
    @Nonnull
    public static LangTag get(@JsonProperty("languageCode") @Nonnull String langTag) {
        return new AutoValue_LangTag(langTag.trim().toLowerCase());
    }

    public String format() {
        return LanguageTagFormatter.format(getLanguageCode());
    }

    @Nonnull
    public abstract String getLanguageCode();
}
