package edu.stanford.bmir.protege.web.shared.dispatch.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("CreateAnnotationProperties")
public abstract class CreateAnnotationPropertiesResult implements CreateEntitiesInHierarchyResult<OWLAnnotationProperty> {

    @JsonCreator
    public static CreateAnnotationPropertiesResult create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                            @JsonProperty("entities") @Nonnull ImmutableSet<EntityNode> entities,
                                            @JsonProperty("eventList") @Nonnull EventList<ProjectEvent<?>> eventList) {
        return new AutoValue_CreateAnnotationPropertiesResult(projectId, eventList, entities);
    }
}
