package edu.stanford.protege.webprotege.forms.data;


import com.fasterxml.jackson.annotation.*;
import edu.stanford.protege.webprotege.entity.OWLEntityData;
import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;

@JsonSubTypes({@JsonSubTypes.Type(FormEntitySubjectDto.class)})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public abstract class FormSubjectDto {

    @Nonnull
    public static FormEntitySubjectDto get(@Nonnull OWLEntityData entity) {
        return new AutoValue_FormEntitySubjectDto(entity);
    }

    public static FormSubjectDto getFormSubject(OWLEntityData root) {
        return FormSubjectDto.get(root);
    }

    @JsonIgnore
    @Nonnull
    public abstract IRI getIri();

    @Nonnull
    public abstract FormEntitySubject toFormSubject();
}
