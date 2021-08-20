package edu.stanford.protege.webprotege.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-28
 */
@AutoValue

@JsonTypeName("GetEntityCreationForms")
public abstract class GetEntityCreationFormsAction implements ProjectAction<GetEntityCreationFormsResult> {

    public static final String CHANNEL = "forms.GetEntityCreationForms";

    @JsonCreator
    public static GetEntityCreationFormsAction create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                                      @JsonProperty("parentEntity") @Nonnull OWLEntity parentEntity,
                                                      @JsonProperty("entityType") @Nonnull EntityType<?> entityType) {
        return new AutoValue_GetEntityCreationFormsAction(projectId, entityType, parentEntity);
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract EntityType<?> getEntityType();

    @Nonnull
    public abstract OWLEntity getParentEntity();
}
