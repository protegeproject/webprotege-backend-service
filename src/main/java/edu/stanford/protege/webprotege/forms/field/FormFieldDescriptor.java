package edu.stanford.protege.webprotege.forms.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.forms.ExpansionState;
import edu.stanford.protege.webprotege.forms.HasFormFieldId;
import edu.stanford.protege.webprotege.common.LanguageMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.protege.webprotege.forms.field.FormFieldDescriptor.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
@JsonPropertyOrder({ID, OWL_BINDING, LABEL, FIELD_RUN, FORM_CONTROL_DESCRIPTOR, REPEATABILITY, OPTIONALITY, READ_ONLY, HELP})

@AutoValue
public abstract class FormFieldDescriptor implements HasFormFieldId, HasRepeatability, BoundControlDescriptor {


    public static final String ID = "id";

    public static final String OWL_BINDING = "owlBinding";

    public static final String LABEL = "label";

    public static final String FIELD_RUN = "fieldRun";

    public static final String FORM_CONTROL_DESCRIPTOR = "formControlDescriptor";

    public static final String REPEATABILITY = "repeatability";

    public static final String OPTIONALITY = "optionality";

    public static final String READ_ONLY = "readOnly";

    public static final String INITIAL_EXPANSIONS_STATE = "initialExpansionState";

    public static final String HELP = "help";

    public static final String DEPRECATION_STRATEGY = "deprecationStrategy";

    @Nonnull
    public static FormFieldDescriptor get(@Nonnull FormFieldId id,
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
    public static FormFieldDescriptor getFromJson(@JsonProperty(ID) @Nonnull String id,
                                                  @JsonProperty(OWL_BINDING) @Nullable OwlBinding owlBinding,
                                                  @JsonProperty(LABEL) @Nullable LanguageMap formLabel,
                                                  @JsonProperty(FIELD_RUN) @Nullable FieldRun fieldRun,
                                                  @JsonProperty(DEPRECATION_STRATEGY) @Nullable FormFieldDeprecationStrategy deprecationStrategy,
                                                  @JsonProperty(FORM_CONTROL_DESCRIPTOR) @Nonnull FormControlDescriptor fieldDescriptor,
                                                  @JsonProperty(REPEATABILITY) @Nullable Repeatability repeatability,
                                                  @JsonProperty(OPTIONALITY) @Nullable Optionality optionality,
                                                  @JsonProperty(READ_ONLY) boolean readOnly,
                                                  @JsonProperty(INITIAL_EXPANSIONS_STATE) @Nullable ExpansionState expansionState,
                                                  @JsonProperty(HELP) @Nullable LanguageMap help) {
        final FormFieldId formFieldId = FormFieldId.get(checkNotNull(id));
        return get(formFieldId, owlBinding, formLabel, fieldRun, deprecationStrategy, fieldDescriptor, repeatability, optionality, readOnly, expansionState, help);
    }

    @Nonnull
    @Override
    @JsonIgnore
    public abstract FormFieldId getId();

    @JsonProperty("id")
    protected String getFormFieldId() {
        return getId().getId();
    }

    @JsonIgnore
    @Nullable
    protected abstract OwlBinding getOwlBindingInternal();

    @Override
    @Nonnull
    public Optional<OwlBinding> getOwlBinding() {
        return Optional.ofNullable(getOwlBindingInternal());
    }

    @Nonnull
    public abstract LanguageMap getLabel();

    @Nonnull
    public abstract FieldRun getFieldRun();

    @Override
    @Nonnull
    public abstract FormControlDescriptor getFormControlDescriptor();

    @Nonnull
    public abstract Optionality getOptionality();

    @Nonnull
    public abstract Repeatability getRepeatability();

    @JsonProperty(DEPRECATION_STRATEGY)
    @Nonnull
    public abstract FormFieldDeprecationStrategy getDeprecationStrategy();

    public abstract boolean isReadOnly();

    @JsonProperty(INITIAL_EXPANSIONS_STATE)
    @Nonnull
    public abstract ExpansionState getInitialExpansionState();

    @Nonnull
    public abstract LanguageMap getHelp();

    @JsonIgnore
    public boolean isComposite() {
        return getFormControlDescriptor() instanceof SubFormControlDescriptor;
    }
}
