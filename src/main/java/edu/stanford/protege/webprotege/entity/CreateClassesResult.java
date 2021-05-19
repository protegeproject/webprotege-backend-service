package edu.stanford.protege.webprotege.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.event.EventList;
import edu.stanford.protege.webprotege.event.ProjectEvent;
import edu.stanford.protege.webprotege.project.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/02/2013
 */
@AutoValue

@JsonTypeName("CreateClasses")
public abstract class CreateClassesResult implements CreateEntitiesInHierarchyResult<OWLClass> {

    @JsonCreator
    public static CreateClassesResult create(@JsonProperty("projectId") ProjectId projectId,
                               @JsonProperty("entities") ImmutableSet<EntityNode> classes,
                               @JsonProperty("eventList") EventList<ProjectEvent<?>> eventList) {
        return new AutoValue_CreateClassesResult(projectId, eventList, classes);
    }
}
