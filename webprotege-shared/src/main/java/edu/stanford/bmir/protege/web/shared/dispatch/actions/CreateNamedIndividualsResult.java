package edu.stanford.bmir.protege.web.shared.dispatch.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/09/2013
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("CreateNamedIndividuals")
public abstract class CreateNamedIndividualsResult implements AbstractCreateEntityResult<OWLNamedIndividual> {

    @JsonCreator
    public static CreateNamedIndividualsResult create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                                      @JsonProperty("eventList") @Nonnull EventList<ProjectEvent<?>> eventList,
                                                      @JsonProperty("entities") ImmutableSet<EntityNode> entities) {
        return new AutoValue_CreateNamedIndividualsResult(projectId, eventList, entities);
    }
}
