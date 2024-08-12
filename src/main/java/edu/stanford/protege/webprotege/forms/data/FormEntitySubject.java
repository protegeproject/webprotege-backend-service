package edu.stanford.protege.webprotege.forms.data;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.forms.PropertyNames;
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
    public static FormEntitySubject get(@JsonProperty(PropertyNames.ENTITY) @Nonnull OWLEntity entity) {
        return new AutoValue_FormEntitySubject(entity);
    }

    @JsonProperty(PropertyNames.ENTITY)
    @Nonnull
    public abstract OWLEntity getEntity();

    @JsonIgnore
    @Nonnull
    @Override
    public IRI getIri() {
        return getEntity().getIRI();
    }
}
