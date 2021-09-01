package edu.stanford.protege.webprotege.forms.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.forms.ExpansionState;
import edu.stanford.protege.webprotege.forms.HasFormFieldId;
import edu.stanford.protege.webprotege.common.LanguageMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;


@AutoValue
public abstract class FormFieldDescriptorDto implements  HasFormFieldId {

    @JsonCreator
    @Nonnull
    public static FormFieldDescriptorDto get(@JsonProperty("id") FormFieldId formFieldId,
                                             @JsonProperty("owlBinding") OwlBinding owlBinding,
                                               @JsonProperty("label")  LanguageMap newlabel,
                                              @JsonProperty("fieldRun")   FieldRun fieldRun,
                                              @JsonProperty("formControlDescriptor")   FormControlDescriptorDto descriptorDto,
                                              @JsonProperty("optionality")   Optionality optionality,
                                              @JsonProperty("repeatability")   Repeatability repeatability,
                                              @JsonProperty("deprecationStrategy")   FormFieldDeprecationStrategy deprecationStrategy,
                                              @JsonProperty("readOnly")   boolean newReadOnly,
                                              @JsonProperty("initialExpansionState")   ExpansionState initialExpansionState,
                                              @JsonProperty("help")   LanguageMap help) {
        return new AutoValue_FormFieldDescriptorDto(formFieldId,
                                                    owlBinding,
                                                    newlabel,
                                                    fieldRun,
                                                    descriptorDto,
                                                    optionality,
                                                    repeatability,
                                                    deprecationStrategy,
                                                    newReadOnly,
                                                    initialExpansionState,
                                                    help);
    }


    @Nonnull
    @Override
    @JsonProperty("id")
    public abstract FormFieldId getId();

    @JsonIgnore
    @Nullable
    protected abstract OwlBinding getOwlBindingInternal();

    @Nonnull
    public Optional<OwlBinding> getOwlBinding() {
        return Optional.ofNullable(getOwlBindingInternal());
    }

    @Nonnull
    public abstract LanguageMap getLabel();

    @Nonnull
    public abstract FieldRun getFieldRun();

    @Nonnull
    public abstract FormControlDescriptorDto getFormControlDescriptor();

    @Nonnull
    public abstract Optionality getOptionality();

    @Nonnull
    public abstract Repeatability getRepeatability();

    @Nonnull
    public abstract FormFieldDeprecationStrategy getDeprecationStrategy();

    public abstract boolean isReadOnly();

    @Nonnull
    public abstract ExpansionState getInitialExpansionState();

    @Nonnull
    public abstract LanguageMap getHelp();

    @JsonIgnore
    public boolean isComposite() {
        return getFormControlDescriptor() instanceof SubFormControlDescriptor;
    }

    public FormFieldDescriptor toFormFieldDescriptor() {
        return FormFieldDescriptor.get(
                getId(),
                getOwlBindingInternal(),
                getLabel(),
                getFieldRun(),
                getDeprecationStrategy(),
                getFormControlDescriptor().toFormControlDescriptor(),
                getRepeatability(),
                getOptionality(),
                isReadOnly(),
                getInitialExpansionState(),
                getHelp()
        );
    }
}
