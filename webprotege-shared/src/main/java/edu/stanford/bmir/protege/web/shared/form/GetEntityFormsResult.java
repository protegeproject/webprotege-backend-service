package edu.stanford.bmir.protege.web.shared.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataDto;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-01
 */
@AutoValue
@GwtCompatible(serializable = true)
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
