package edu.stanford.protege.webprotege.shortform;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableMap;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-07
 */
@AutoValue
public abstract class EntityShortForms {

    @Nonnull
    public static EntityShortForms get(@Nonnull OWLEntity entity,
                                       @Nonnull ImmutableMap<DictionaryLanguage, String> shortForms) {
        return new AutoValue_EntityShortForms(entity, shortForms);
    }

    @Nonnull
    public abstract OWLEntity getEntity();

    @Nonnull
    public abstract ImmutableMap<DictionaryLanguage, String> getShortForms();
}
