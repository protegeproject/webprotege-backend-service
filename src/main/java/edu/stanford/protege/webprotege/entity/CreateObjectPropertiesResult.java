package edu.stanford.protege.webprotege.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.event.EventList;
import edu.stanford.protege.webprotege.event.ProjectEvent;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */
@AutoValue

@JsonTypeName("CreateObjectProperties")
public abstract class CreateObjectPropertiesResult implements CreateEntitiesInHierarchyResult<OWLObjectProperty> {

    @JsonCreator
    public static CreateObjectPropertiesResult create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                        @JsonProperty("entities") @Nonnull ImmutableSet<EntityNode> entities,
                                        @JsonProperty("eventList") EventList<ProjectEvent<?>> eventList) {
        return new AutoValue_CreateObjectPropertiesResult(projectId, eventList, entities);
    }
}
