package edu.stanford.bmir.protege.web.shared.dispatch.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableCollection;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-30
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("CreateEntityFromFormData")
public abstract class CreateEntityFromFormDataResult implements AbstractCreateEntityResult<OWLEntity> {

    @JsonCreator
    public static CreateEntityFromFormDataResult create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                                        @JsonProperty("eventList") @Nonnull EventList<ProjectEvent<?>> eventList,
                                                        @JsonProperty("entities") ImmutableCollection<EntityNode> entities) {

        return new AutoValue_CreateEntityFromFormDataResult(projectId, eventList, entities);
    }
}
