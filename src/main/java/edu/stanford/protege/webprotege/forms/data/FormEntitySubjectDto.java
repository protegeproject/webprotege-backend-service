package edu.stanford.protege.webprotege.forms.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import edu.stanford.protege.webprotege.entity.OWLEntityData;
import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;

@AutoValue

@JsonTypeName("FormEntitySubjectDto")
public abstract class FormEntitySubjectDto extends FormSubjectDto {

    @JsonCreator
    @Nonnull
    public static FormEntitySubjectDto get(@JsonProperty("entityData") @Nonnull OWLEntityData entityData) {
        return new AutoValue_FormEntitySubjectDto(entityData);
    }


    @Nonnull
    public abstract OWLEntityData getEntityData();

    @JsonIgnore
    @Nonnull
    @Override
    public IRI getIri() {
        return getEntityData().getEntity().getIRI();
    }

    @Memoized
    @Nonnull
    @Override
    public FormEntitySubject toFormSubject() {
        return FormEntitySubject.get(getEntityData().getEntity());
    }
}
