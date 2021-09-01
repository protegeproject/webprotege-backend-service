package edu.stanford.protege.webprotege.forms.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.forms.FormDescriptorDto;
import edu.stanford.protege.webprotege.forms.FormId;
import edu.stanford.protege.webprotege.common.Page;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

import static com.google.common.collect.ImmutableList.toImmutableList;

@AutoValue

@JsonTypeName("FormDataDto")
public abstract class FormDataDto implements FormControlDataDto {

    @JsonCreator
    @Nonnull
    public static FormDataDto get(@JsonProperty("subject") @Nonnull FormSubjectDto subject,
                                  @JsonProperty("formDescriptor") @Nonnull FormDescriptorDto formDescriptor,
                                  @JsonProperty("formFieldData") @Nonnull ImmutableList<FormFieldDataDto> formFieldData,
                                  @JsonProperty("depth") int depth) {
        return new AutoValue_FormDataDto(depth, subject, formDescriptor, formFieldData);
    }

    /**
     * Create a form with empty data
     * @param subject The form subject
     * @param formDescriptor The form descriptor
     * @param depth The depth of nesting
     */
    @Nonnull
    public static FormDataDto get(@Nonnull FormSubjectDto subject,
                                  @Nonnull FormDescriptorDto formDescriptor,
                                  int depth) {
        ImmutableList<FormFieldDataDto> emptyFields = formDescriptor.getFields()
                                                                .stream()
                                                                .map(field -> FormFieldDataDto.get(field,
                                                                                                   Page.emptyPage()))
                                                                .collect(toImmutableList());
        return new AutoValue_FormDataDto(depth, subject, formDescriptor, emptyFields);
    }

    /**
     * Create a form descriptor that does not have a subject.
     */
    @Nonnull
    public static FormDataDto get(@Nonnull FormDescriptorDto formDescriptor,
                                  @Nonnull ImmutableList<FormFieldDataDto> formFieldData,
                                  int depth) {
        return new AutoValue_FormDataDto(depth, null, formDescriptor, formFieldData);
    }

    @Nonnull
    public Optional<FormSubjectDto> getSubject() {
        return Optional.ofNullable(getSubjectInternal());
    }

    @JsonIgnore
    @Nullable
    protected abstract FormSubjectDto getSubjectInternal();

    public abstract FormDescriptorDto getFormDescriptor();

    public abstract ImmutableList<FormFieldDataDto> getFormFieldData();

    @Override
    public <R> R accept(FormControlDataDtoVisitorEx<R> visitor) {
        return visitor.visit(this);
    }

    @Nonnull
    @Override
    public FormControlData toFormControlData() {
        return FormData.get(getSubject().map(FormSubjectDto::toFormSubject),
                getFormDescriptor().toFormDescriptor(),
                getFormFieldData().stream().map(FormFieldDataDto::getFormFieldData).collect(toImmutableList()));
    }

    @Nonnull
    public FormData toFormData() {
        return FormData.get(getSubject().map(FormSubjectDto::toFormSubject),
                getFormDescriptor().toFormDescriptor(),
                getFormFieldData().stream().map(FormFieldDataDto::toFormFieldData).collect(toImmutableList()));
    }

    @Nonnull
    public FormId getFormId() {
        return getFormDescriptor().getFormId();
    }
}
