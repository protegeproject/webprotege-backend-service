package edu.stanford.bmir.protege.web.server.watches;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.event.HasEventList;
import edu.stanford.bmir.protege.web.server.event.ProjectEvent;
import edu.stanford.bmir.protege.web.server.event.EventList;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/03/2013
 */
public class RemoveWatchesResult  implements Result, HasEventList<ProjectEvent<?>> {

    private EventList<ProjectEvent<?>> eventList;

    @JsonCreator
    public RemoveWatchesResult(@JsonProperty("eventList") EventList<ProjectEvent<?>> eventList) {
        this.eventList = checkNotNull(eventList);
    }

    private RemoveWatchesResult() {
    }

    @Override
    public EventList<ProjectEvent<?>> getEventList() {
        return eventList;
    }
}
