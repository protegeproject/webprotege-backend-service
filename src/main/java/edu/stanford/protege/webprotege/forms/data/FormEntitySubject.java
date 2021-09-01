package edu.stanford.protege.webprotege.forms.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-13
 */
@AutoValue

@JsonTypeName("Entity")
public abstract class FormEntitySubject implements FormSubject {

    @JsonCreator
    public static FormEntitySubject get(@JsonProperty("entity") @Nonnull OWLEntity entity) {
        return new AutoValue_FormEntitySubject(entity);
    }

    @Nonnull
    public abstract OWLEntity getEntity();

    @Nonnull
    @Override
    public IRI getIri() {
        return getEntity().getIRI();
    }
}
