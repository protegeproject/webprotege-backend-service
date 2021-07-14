package edu.stanford.protege.webprotege.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.entity.OWLEntityData;
import edu.stanford.protege.webprotege.form.data.FormDataDto;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-01
 */
@AutoValue

@JsonTypeName("GetEntityForms")
public abstract class GetEntityFormsResult implements Result {

    @JsonCreator
    public static GetEntityFormsResult create(@JsonProperty("entity") @Nonnull OWLEntityData entityData,
                                              @JsonProperty("filteredFormIds") @Nonnull ImmutableList<FormId> filteredFormIds,
                                              @JsonProperty("formData") @Nonnull ImmutableList<FormDataDto> formData) {
        return new AutoValue_GetEntityFormsResult(entityData, filteredFormIds, formData);
    }

    @JsonProperty("entity")
    @Nonnull
    public abstract OWLEntityData getEntityData();

    @Nonnull
    public abstract ImmutableList<FormId> getFilteredFormIds();

    @Nonnull
    public abstract ImmutableList<FormDataDto> getFormData();
}
