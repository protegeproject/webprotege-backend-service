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

    public static final String ID = "id";

    public static final String DESCRIPTION = "description";

    public static final String FORM_IDS = "formIds";

    @JsonCreator
    @Nonnull
    public static FormGroup get(@Nonnull @JsonProperty(ID) FormGroupId formGroupId,
                                @Nonnull @JsonProperty(DESCRIPTION) String description,
                                @Nonnull @JsonProperty(FORM_IDS) ImmutableList<FormId> formIds) {
        return new AutoValue_FormGroup(formGroupId,
                                       description,
                                       formIds);
    }

    @JsonProperty(ID)
    @Nonnull
    public abstract FormGroupId getId();

    @JsonProperty(DESCRIPTION)
    @Nonnull
    public abstract String getDescription();

    @JsonProperty(FORM_IDS)
    @Nonnull
    public abstract ImmutableList<FormId> getFormIds();
}
