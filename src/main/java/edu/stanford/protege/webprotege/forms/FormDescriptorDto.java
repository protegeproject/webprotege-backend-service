package edu.stanford.protege.webprotege.forms;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.forms.field.FormFieldDescriptorDto;
import edu.stanford.protege.webprotege.common.LanguageMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

import static com.google.common.collect.ImmutableList.toImmutableList;


@AutoValue
public abstract class FormDescriptorDto {

    @JsonCreator
    public static FormDescriptorDto get(@JsonProperty("formId") @Nonnull FormId formId,
                                        @JsonProperty("label") @Nonnull LanguageMap label,
                                        @JsonProperty("fields") @Nonnull ImmutableList<FormFieldDescriptorDto> fields,
                                        @JsonProperty("formSubjectFactoryDescriptor") @Nullable FormSubjectFactoryDescriptor subjectFactoryDescriptor) {
        return new AutoValue_FormDescriptorDto(formId, label, fields, subjectFactoryDescriptor);
    }

    @Nonnull
    public abstract FormId getFormId();

    @Nonnull
    public abstract LanguageMap getLabel();

    @Nonnull
    public abstract ImmutableList<FormFieldDescriptorDto> getFields();

    @Nullable
    protected abstract FormSubjectFactoryDescriptor getFormSubjectFactoryDescriptorInternal();

    public Optional<FormSubjectFactoryDescriptor> getFormSubjectFactoryDescriptor() {
        return Optional.ofNullable(getFormSubjectFactoryDescriptorInternal());
    }

    @Nonnull
    public FormDescriptor toFormDescriptor() {
        return new FormDescriptor(getFormId(),
                                  getLabel(),
                                  getFields().stream().map(FormFieldDescriptorDto::toFormFieldDescriptor).collect(toImmutableList()),
                                  getFormSubjectFactoryDescriptor());
    }
}
