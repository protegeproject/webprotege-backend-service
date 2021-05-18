package edu.stanford.bmir.protege.web.server.hierarchy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;


import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.event.EventList;
import edu.stanford.bmir.protege.web.server.event.HasEventList;
import edu.stanford.bmir.protege.web.server.event.ProjectEvent;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 8 Dec 2017
 */
@AutoValue

@JsonTypeName("MoveHierarchyNode")
public abstract class MoveHierarchyNodeResult implements Result, HasEventList<ProjectEvent<?>> {


    @JsonCreator
    public static MoveHierarchyNodeResult create(@JsonProperty("moved") boolean moved,
                                                 @JsonProperty("eventList") EventList<ProjectEvent<?>> eventList) {
        return new AutoValue_MoveHierarchyNodeResult(moved, eventList);
    }

    public abstract boolean isMoved();

    @Override
    public abstract EventList<ProjectEvent<?>> getEventList();

}
