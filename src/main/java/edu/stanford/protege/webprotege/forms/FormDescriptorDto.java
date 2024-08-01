package edu.stanford.protege.webprotege.forms;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.common.LanguageMap;
import edu.stanford.protege.webprotege.forms.field.FormFieldDescriptorDto;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

import static com.google.common.collect.ImmutableList.toImmutableList;


@AutoValue
public abstract class FormDescriptorDto {

    @JsonCreator
    public static FormDescriptorDto get(@JsonProperty(PropertyNames.FORM_ID) @Nonnull FormId formId,
                                        @JsonProperty(PropertyNames.LABEL) @Nonnull LanguageMap label,
                                        @JsonProperty(PropertyNames.FIELDS) @Nonnull ImmutableList<FormFieldDescriptorDto> fields,
                                        @JsonProperty(PropertyNames.SUBJECT_FACTORY) @Nullable FormSubjectFactoryDescriptor subjectFactoryDescriptor) {
        return new AutoValue_FormDescriptorDto(formId, label, fields, subjectFactoryDescriptor);
    }

    @JsonProperty(PropertyNames.FORM_ID)
    @Nonnull
    public abstract FormId getFormId();

    @Nonnull
    @JsonProperty(PropertyNames.LABEL)
    public abstract LanguageMap getLabel();

    @Nonnull
    @JsonProperty(PropertyNames.FIELDS)
    public abstract ImmutableList<FormFieldDescriptorDto> getFields();

    @Nullable
    @JsonProperty(PropertyNames.SUBJECT_FACTORY)
    protected abstract FormSubjectFactoryDescriptor getFormSubjectFactoryDescriptorInternal();

    @JsonIgnore
    public Optional<FormSubjectFactoryDescriptor> getFormSubjectFactoryDescriptor() {
        return Optional.ofNullable(getFormSubjectFactoryDescriptorInternal());
    }

    @Nonnull
    public FormDescriptor toFormDescriptor() {
        return new FormDescriptor(getFormId(),
                                  getLabel(),
                                  getFields().stream()
                                             .map(FormFieldDescriptorDto::toFormFieldDescriptor)
                                             .collect(toImmutableList()),
                                  getFormSubjectFactoryDescriptor());
    }
}
