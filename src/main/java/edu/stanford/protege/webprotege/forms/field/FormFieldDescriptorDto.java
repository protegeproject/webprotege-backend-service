package edu.stanford.protege.webprotege.forms.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.common.LanguageMap;
import edu.stanford.protege.webprotege.forms.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;


@AutoValue
public abstract class FormFieldDescriptorDto implements HasFormRegionId {

    @JsonCreator
    @Nonnull
    public static FormFieldDescriptorDto get(@JsonProperty(PropertyNames.ID) FormRegionId formRegionId,
                                             @JsonProperty(PropertyNames.OWL_BINDING) OwlBinding owlBinding,
                                             @JsonProperty(PropertyNames.LABEL) LanguageMap newlabel,
                                             @JsonProperty(PropertyNames.FIELD_RUN) FieldRun fieldRun,
                                             @JsonProperty(PropertyNames.CONTROL) FormControlDescriptorDto descriptorDto,
                                             @JsonProperty(PropertyNames.OPTIONALITY) Optionality optionality,
                                             @JsonProperty(PropertyNames.REPEATABILITY) Repeatability repeatability,
                                             @JsonProperty(PropertyNames.DEPRECATION_STRATEGY) FormFieldDeprecationStrategy deprecationStrategy,
                                             @JsonProperty(PropertyNames.READ_ONLY) boolean newReadOnly,
                                             @JsonProperty(PropertyNames.INITIAL_EXPANSIONS_STATE) ExpansionState initialExpansionState,
                                             @JsonProperty(PropertyNames.HELP) LanguageMap help) {
        return new AutoValue_FormFieldDescriptorDto(formRegionId,
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
    @JsonProperty(PropertyNames.ID)
    public abstract FormRegionId getId();

    @JsonProperty(PropertyNames.OWL_BINDING)
    @Nullable
    protected abstract OwlBinding getOwlBindingInternal();

    @JsonIgnore
    @Nonnull
    public Optional<OwlBinding> getOwlBinding() {
        return Optional.ofNullable(getOwlBindingInternal());
    }

    @Nonnull
    @JsonProperty(PropertyNames.LABEL)
    public abstract LanguageMap getLabel();

    @Nonnull
    @JsonProperty(PropertyNames.FIELD_RUN)
    public abstract FieldRun getFieldRun();

    @JsonProperty(PropertyNames.CONTROL)
    @Nonnull
    public abstract FormControlDescriptorDto getFormControlDescriptor();

    @Nonnull
    @JsonProperty(PropertyNames.OPTIONALITY)
    public abstract Optionality getOptionality();

    @Nonnull
    @JsonProperty(PropertyNames.REPEATABILITY)
    public abstract Repeatability getRepeatability();

    @Nonnull
    @JsonProperty(PropertyNames.DEPRECATION_STRATEGY)
    public abstract FormFieldDeprecationStrategy getDeprecationStrategy();

    @JsonProperty(PropertyNames.READ_ONLY)
    public abstract boolean isReadOnly();

    @Nonnull
    @JsonProperty(PropertyNames.INITIAL_EXPANSIONS_STATE)
    public abstract ExpansionState getInitialExpansionState();

    @Nonnull
    @JsonProperty(PropertyNames.HELP)
    public abstract LanguageMap getHelp();

    @JsonIgnore
    public boolean isComposite() {
        return getFormControlDescriptor() instanceof SubFormControlDescriptor;
    }

    public FormFieldDescriptor toFormFieldDescriptor() {
        return FormFieldDescriptor.get(getId(),
                                       getOwlBindingInternal(),
                                       getLabel(),
                                       getFieldRun(),
                                       getDeprecationStrategy(),
                                       getFormControlDescriptor().toFormControlDescriptor(),
                                       getRepeatability(),
                                       getOptionality(),
                                       isReadOnly(),
                                       getInitialExpansionState(),
                                       getHelp());
    }
}
