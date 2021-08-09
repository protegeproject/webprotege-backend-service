package edu.stanford.protege.webprotege.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.form.data.FormData;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-30
 */
@AutoValue

@JsonTypeName("CreateEntityFromFormData")
public abstract class CreateEntityFromFormDataAction implements ProjectAction<CreateEntityFromFormDataResult> {

    @JsonCreator
    public static CreateEntityFromFormDataAction create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                          @JsonProperty("entityType") @Nonnull EntityType<?> entityType,
                                          @JsonProperty("freshEntitIri") @Nonnull FreshEntityIri freshEntityIri,
                                          @JsonProperty("formData") @Nonnull FormData formData) {
        return new AutoValue_CreateEntityFromFormDataAction(projectId, entityType, freshEntityIri, formData);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    public abstract EntityType<?> getEntityType();

    public abstract FreshEntityIri getFreshEntityIri();

    public abstract FormData getFormData();
}
