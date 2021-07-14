package edu.stanford.protege.webprotege.bulkop;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Sep 2018
 */
@AutoValue

public abstract class NewAnnotationData {

    @JsonCreator
    public static NewAnnotationData get(@JsonProperty("property") @Nonnull Optional<OWLAnnotationProperty> property,
                                        @JsonProperty("value") @Nonnull Optional<String> value,
                                        @JsonProperty("languageTag") @Nonnull Optional<String> languageTag) {
        return new AutoValue_NewAnnotationData(property.orElse(null),
                                           value.orElse(null),
                                           languageTag.orElse(null));
    }

    @Nullable
    protected abstract OWLAnnotationProperty property();

    @Nonnull
    public Optional<OWLAnnotationProperty> getProperty() {
        return Optional.ofNullable(property());
    }

    @Nullable
    protected abstract String value();

    @Nonnull
    public Optional<String> getValue() {
        return Optional.ofNullable(value());
    }

    @Nullable
    protected abstract String languageTag();

    @Nonnull
    public Optional<String> getLanguageTag() {
        return Optional.ofNullable(languageTag());
    }
}
