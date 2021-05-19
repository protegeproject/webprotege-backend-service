package edu.stanford.protege.webprotege.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableCollection;
import edu.stanford.protege.webprotege.event.EventList;
import edu.stanford.protege.webprotege.event.ProjectEvent;
import edu.stanford.protege.webprotege.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-30
 */
@AutoValue

@JsonTypeName("CreateEntityFromFormData")
public abstract class CreateEntityFromFormDataResult implements AbstractCreateEntityResult<OWLEntity> {

    @JsonCreator
    public static CreateEntityFromFormDataResult create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                                        @JsonProperty("eventList") @Nonnull EventList<ProjectEvent<?>> eventList,
                                                        @JsonProperty("entities") ImmutableCollection<EntityNode> entities) {

        return new AutoValue_CreateEntityFromFormDataResult(projectId, eventList, entities);
    }
}
