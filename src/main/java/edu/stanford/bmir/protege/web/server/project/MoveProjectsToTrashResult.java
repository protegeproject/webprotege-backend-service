package edu.stanford.bmir.protege.web.server.project;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.event.EventList;
import edu.stanford.bmir.protege.web.server.event.HasEventList;
import edu.stanford.bmir.protege.web.server.event.WebProtegeEvent;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/04/2013
 */
@AutoValue

@JsonTypeName("MoveProjectsToTrash")
public abstract class MoveProjectsToTrashResult implements Result, HasEventList<WebProtegeEvent<?>> {

    @JsonCreator
    public static MoveProjectsToTrashResult create(@JsonProperty("eventList") EventList<WebProtegeEvent<?>> eventList) {
        return new AutoValue_MoveProjectsToTrashResult(eventList);
    }
}
