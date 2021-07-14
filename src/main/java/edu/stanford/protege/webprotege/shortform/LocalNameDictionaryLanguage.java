package edu.stanford.protege.webprotege.shortform;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-05
 */

@AutoValue
@JsonTypeName(LocalNameDictionaryLanguage.TYPE_NAME)
public abstract class LocalNameDictionaryLanguage extends DictionaryLanguage {

    public static final String TYPE_NAME = "LocalName";

    @JsonCreator
    public static LocalNameDictionaryLanguage get() {
        return new AutoValue_LocalNameDictionaryLanguage();
    }

    @Override
    public boolean isAnnotationBased() {
        return false;
    }

    @Override
    public <R> R accept(@Nonnull DictionaryLanguageVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Nonnull
    @Override
    public String getLang() {
        return "";
    }
}
