package edu.stanford.protege.webprotege.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.dispatch.Result;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-28
 */
@AutoValue

@JsonTypeName("GetEntityCreationForms")
public abstract class GetEntityCreationFormsResult implements Result {

    @JsonCreator
    @Nonnull
    public static GetEntityCreationFormsResult get(@JsonProperty("formDescriptors") @Nonnull ImmutableList<FormDescriptorDto> formDtos) {
        return new AutoValue_GetEntityCreationFormsResult(formDtos);
    }

    @Nonnull
    public abstract ImmutableList<FormDescriptorDto> getFormDescriptors();


}
