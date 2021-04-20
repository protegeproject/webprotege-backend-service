package edu.stanford.bmir.protege.web.shared.project;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.event.*;

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
