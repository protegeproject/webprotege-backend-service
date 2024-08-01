package edu.stanford.protege.webprotege.forms.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-13
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({@Type(FormEntitySubject.class)})
public interface FormSubject {

    static FormEntitySubject get(@Nonnull OWLEntity entity) {
        return FormEntitySubject.get(entity);
    }

    @JsonIgnore
    @Nonnull
    IRI getIri();

}
