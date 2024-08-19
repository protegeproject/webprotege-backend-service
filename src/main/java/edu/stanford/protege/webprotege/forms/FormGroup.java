package edu.stanford.protege.webprotege.forms;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-27
 */
@AutoValue

public abstract class FormGroup {

    @JsonCreator
    @Nonnull
    public static FormGroup get(@Nonnull @JsonProperty(PropertyNames.ID) FormGroupId formGroupId,
                                @Nonnull @JsonProperty(PropertyNames.DESCRIPTION) String description,
                                @Nonnull @JsonProperty(PropertyNames.FORMS) ImmutableList<FormId> formIds) {
        return new AutoValue_FormGroup(formGroupId, description, formIds);
    }

    @JsonProperty(PropertyNames.ID)
    @Nonnull
    public abstract FormGroupId getId();

    @JsonProperty(PropertyNames.DESCRIPTION)
    @Nonnull
    public abstract String getDescription();

    @JsonProperty(PropertyNames.FORMS)
    @Nonnull
    public abstract ImmutableList<FormId> getFormIds();
}
