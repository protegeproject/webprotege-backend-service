package edu.stanford.protege.webprotege.forms.data;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.stanford.protege.webprotege.entity.OWLEntityData;
import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;

@JsonSubTypes({
        @JsonSubTypes.Type(FormEntitySubjectDto.class)
})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public abstract class FormSubjectDto {

    @Nonnull
    public static FormEntitySubjectDto get(@Nonnull OWLEntityData entity) {
        return new AutoValue_FormEntitySubjectDto(entity);
    }

    @JsonIgnore
    @Nonnull
    public abstract IRI getIri();

    @Nonnull
    public abstract FormEntitySubject toFormSubject();

    public static FormSubjectDto getFormSubject(OWLEntityData root) {
        return FormSubjectDto.get(root);
    }
}
