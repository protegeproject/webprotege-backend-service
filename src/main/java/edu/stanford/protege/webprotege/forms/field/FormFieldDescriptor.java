package edu.stanford.protege.webprotege.forms.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.common.LanguageMap;
import edu.stanford.protege.webprotege.forms.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.protege.webprotege.forms.PropertyNames.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
@JsonPropertyOrder({ID, OWL_BINDING, LABEL, FIELD_RUN, CONTROL, REPEATABILITY, OPTIONALITY, READ_ONLY, HELP})

@AutoValue
public abstract class FormFieldDescriptor implements HasFormRegionId, HasRepeatability, BoundControlDescriptor {


    @Nonnull
    public static FormFieldDescriptor get(@Nonnull FormRegionId id,
                                          @Nullable OwlBinding owlBinding,
                                          @Nullable LanguageMap formLabel,
                                          @Nullable FieldRun fieldRun,
                                          @Nullable FormFieldDeprecationStrategy deprecationStrategy,
                                          @Nonnull FormControlDescriptor fieldDescriptor,
                                          Repeatability repeatability,
                                          Optionality optionality,
                                          boolean readOnly,
                                          @Nullable ExpansionState expansionState,
                                          @Nullable LanguageMap help) {
        return new AutoValue_FormFieldDescriptor(id,
                                                 owlBinding,
                                                 formLabel == null ? LanguageMap.empty() : formLabel,
                                                 fieldRun == null ? FieldRun.START : fieldRun,
                                                 fieldDescriptor,
                                                 optionality == null ? Optionality.REQUIRED : optionality,
                                                 repeatability == null ? Repeatability.NON_REPEATABLE : repeatability,
                                                 deprecationStrategy == null ? FormFieldDeprecationStrategy.DELETE_VALUES : deprecationStrategy,
                                                 readOnly,
                                                 expansionState == null ? ExpansionState.EXPANDED : expansionState,
                                                 help == null ? LanguageMap.empty() : help);
    }

    @JsonCreator
    @Nonnull
    public static FormFieldDescriptor getFromJson(@JsonProperty(ID) @Nonnull FormRegionId id,
                                                  @JsonProperty(OWL_BINDING) @Nullable OwlBinding owlBinding,
                                                  @JsonProperty(LABEL) @Nullable LanguageMap formLabel,
                                                  @JsonProperty(FIELD_RUN) @Nullable FieldRun fieldRun,
                                                  @JsonProperty(DEPRECATION_STRATEGY) @Nullable FormFieldDeprecationStrategy deprecationStrategy,
                                                  @JsonProperty(CONTROL) @Nonnull FormControlDescriptor fieldDescriptor,
                                                  @JsonProperty(REPEATABILITY) @Nullable Repeatability repeatability,
                                                  @JsonProperty(OPTIONALITY) @Nullable Optionality optionality,
                                                  @JsonProperty(READ_ONLY) boolean readOnly,
                                                  @JsonProperty(INITIAL_EXPANSIONS_STATE) @Nullable ExpansionState expansionState,
                                                  @JsonProperty(HELP) @Nullable LanguageMap help) {
        return get(id,
                   owlBinding,
                   formLabel,
                   fieldRun,
                   deprecationStrategy,
                   fieldDescriptor,
                   repeatability,
                   optionality,
                   readOnly,
                   expansionState,
                   help);
    }

    @Nonnull
    @Override
    public abstract FormRegionId getId();

    @JsonProperty(OWL_BINDING)
    @Nullable
    protected abstract OwlBinding getOwlBindingInternal();

    @Override
    @Nonnull
    @JsonIgnore
    public Optional<OwlBinding> getOwlBinding() {
        return Optional.ofNullable(getOwlBindingInternal());
    }

    @Nonnull
    @JsonProperty(LABEL)
    public abstract LanguageMap getLabel();

    @Nonnull
    @JsonProperty(FIELD_RUN)
    public abstract FieldRun getFieldRun();

    @Override
    @Nonnull
    @JsonProperty(CONTROL)
    public abstract FormControlDescriptor getFormControlDescriptor();

    @Nonnull
    public abstract Optionality getOptionality();

    @Nonnull
    public abstract Repeatability getRepeatability();

    @JsonProperty(DEPRECATION_STRATEGY)
    @Nonnull
    public abstract FormFieldDeprecationStrategy getDeprecationStrategy();

    @JsonProperty(READ_ONLY)
    public abstract boolean isReadOnly();

    @JsonProperty(INITIAL_EXPANSIONS_STATE)
    @Nonnull
    public abstract ExpansionState getInitialExpansionState();

    @Nonnull
    @JsonProperty(HELP)
    public abstract LanguageMap getHelp();

    @JsonIgnore
    public boolean isComposite() {
        return getFormControlDescriptor() instanceof SubFormControlDescriptor;
    }
}
