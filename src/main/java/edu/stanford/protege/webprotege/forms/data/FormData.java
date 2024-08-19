package edu.stanford.protege.webprotege.forms.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.forms.*;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-06
 */
@AutoValue

@JsonTypeName("FormData")
public abstract class FormData implements FormControlData {

    @JsonCreator
    public static FormData get(@JsonProperty(PropertyNames.SUBJECT) @Nonnull Optional<FormEntitySubject> subject,
                               @JsonProperty(PropertyNames.FORM) @Nonnull FormDescriptor formDescriptor,
                               @JsonProperty(PropertyNames.FIELDS) @Nonnull ImmutableList<FormFieldData> formFieldData) {
        return new AutoValue_FormData(subject.orElse(null), formDescriptor, formFieldData);
    }

    public static FormData empty(@Nonnull OWLEntity entity, @Nonnull FormId formId) {
        return get(Optional.of(FormSubject.get(entity)), FormDescriptor.empty(formId), ImmutableList.of());
    }

    @Override
    public <R> R accept(@Nonnull FormControlDataVisitorEx<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public void accept(@Nonnull FormControlDataVisitor visitor) {
        visitor.visit(this);
    }

    @JsonIgnore
    @Nonnull
    public Optional<FormEntitySubject> getSubject() {
        return Optional.ofNullable(getSubjectInternal());
    }

    @JsonProperty(PropertyNames.SUBJECT)
    @Nullable
    protected abstract FormEntitySubject getSubjectInternal();

    @JsonProperty(PropertyNames.FORM)
    public abstract FormDescriptor getFormDescriptor();

    @JsonProperty(PropertyNames.FIELDS)
    public abstract ImmutableList<FormFieldData> getFormFieldData();

    @Nonnull
    public FormId getFormId() {
        return getFormDescriptor().getFormId();
    }
}
