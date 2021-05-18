package edu.stanford.bmir.protege.web.server.dispatch;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.stanford.bmir.protege.web.server.event.EventList;
import edu.stanford.bmir.protege.web.server.event.HasEventList;
import edu.stanford.bmir.protege.web.server.event.ProjectEvent;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
public class UpdateObjectResult implements Result, HasEventList<ProjectEvent<?>> {

    private EventList<ProjectEvent<?>> events;

    private UpdateObjectResult() {
    }

    @JsonCreator
    public UpdateObjectResult(@JsonProperty("eventList") EventList<ProjectEvent<?>> events) {
        this.events = events;
    }

    @Override
    public EventList<ProjectEvent<?>> getEventList() {
        return events;
    }

}
