package edu.stanford.bmir.protege.web.shared.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-28
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("GetEntityCreationForms")
public abstract class GetEntityCreationFormsAction implements ProjectAction<GetEntityCreationFormsResult> {

    @JsonCreator
    public static GetEntityCreationFormsAction create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                                      @JsonProperty("parentEntity") @Nonnull OWLEntity parentEntity,
                                                      @JsonProperty("entityType") @Nonnull EntityType<?> entityType) {
        return new AutoValue_GetEntityCreationFormsAction(projectId, entityType, parentEntity);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract EntityType<?> getEntityType();

    @Nonnull
    public abstract OWLEntity getParentEntity();
}
