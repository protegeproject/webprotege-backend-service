package edu.stanford.protege.webprotege.forms;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import edu.stanford.protege.webprotege.common.LanguageMap;
import edu.stanford.protege.webprotege.forms.field.FormFieldDescriptor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public class FormDescriptor {

    private final List<FormFieldDescriptor> elements;

    private FormId formId;

    private LanguageMap label = LanguageMap.empty();

    @Nullable
    private FormSubjectFactoryDescriptor subjectFactoryDescriptor;

    private FormDescriptor() {
        this.elements = new ArrayList<>();
    }

    @JsonCreator
    public FormDescriptor(@JsonProperty(PropertyNames.FORM_ID) FormId id,
                          @JsonProperty(PropertyNames.LABEL) LanguageMap label,
                          @JsonProperty(PropertyNames.FIELDS) List<FormFieldDescriptor> formFieldDescriptors,
                          @JsonProperty(PropertyNames.SUBJECT_FACTORY) Optional<FormSubjectFactoryDescriptor> subjectFactoryDescriptor) {
        this.formId = id;
        this.label = label;
        this.elements = new ArrayList<>(formFieldDescriptors);
        this.subjectFactoryDescriptor = subjectFactoryDescriptor.orElse(null);
    }

    public static FormDescriptor empty(FormId formId) {
        return new FormDescriptor(formId, LanguageMap.empty(), Collections.emptyList(), Optional.empty());
    }

    public static Builder builder(FormId formId) {
        return new Builder(formId);
    }

    public FormDescriptor withFields(Predicate<FormFieldDescriptor> test) {
        List<FormFieldDescriptor> filteredFields = elements.stream().filter(test).collect(Collectors.toList());
        return new FormDescriptor(formId, label, filteredFields, getSubjectFactoryDescriptor());
    }

    @JsonProperty(PropertyNames.FORM_ID)
    public FormId getFormId() {
        return formId;
    }

    @JsonProperty(PropertyNames.LABEL)
    public LanguageMap getLabel() {
        return label;
    }

    @JsonProperty(PropertyNames.SUBJECT_FACTORY)
    @Nonnull
    public Optional<FormSubjectFactoryDescriptor> getSubjectFactoryDescriptor() {
        return Optional.ofNullable(subjectFactoryDescriptor);
    }

    @JsonProperty(PropertyNames.FIELDS)
    public List<FormFieldDescriptor> getFields() {
        return elements;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(formId, label, elements);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof FormDescriptor)) {
            return false;
        }
        FormDescriptor other = (FormDescriptor) obj;
        return this.formId.equals(other.formId) && this.label.equals(other.label) && this.elements.equals(other.elements) && Objects.equal(
                this.subjectFactoryDescriptor,
                other.subjectFactoryDescriptor);
    }


    @Override
    public String toString() {
        return toStringHelper("FormDescriptor").addValue(formId).addValue(label).addValue(elements).toString();
    }

    @Nonnull
    public FormDescriptor withFormId(@Nonnull FormId formId) {
        return new FormDescriptor(formId, label, getFields(), getSubjectFactoryDescriptor());
    }

    @Nonnull
    public FormDescriptor withLabel(@Nonnull LanguageMap newLabel) {
        return new FormDescriptor(formId, newLabel, getFields(), getSubjectFactoryDescriptor());
    }

    public static class Builder {

        private final FormId formId;

        private final List<FormFieldDescriptor> builder_elementDescriptors = new ArrayList<>();

        private LanguageMap label = LanguageMap.empty();


        public Builder(FormId formId) {
            this.formId = checkNotNull(formId);
        }

        public Builder setLabel(LanguageMap label) {
            this.label = checkNotNull(label);
            return this;
        }

        public Builder addDescriptor(FormFieldDescriptor descriptor) {
            builder_elementDescriptors.add(descriptor);
            return this;
        }


        public FormDescriptor build() {
            return new FormDescriptor(formId, label, builder_elementDescriptors, Optional.empty());
        }
    }
}
