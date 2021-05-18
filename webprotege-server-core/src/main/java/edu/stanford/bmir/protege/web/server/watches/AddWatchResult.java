package edu.stanford.bmir.protege.web.server.watches;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.event.HasEventList;
import edu.stanford.bmir.protege.web.server.event.ProjectEvent;
import edu.stanford.bmir.protege.web.server.event.EventList;

import java.util.Objects;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/03/2013
 */
public class AddWatchResult implements Result, HasEventList<ProjectEvent<?>> {

    private EventList<ProjectEvent<?>> eventList;

    private AddWatchResult() {
    }

    @JsonCreator
    public AddWatchResult(@JsonProperty("eventList") EventList<ProjectEvent<?>> eventList) {
        this.eventList = checkNotNull(eventList);
    }

    @Override
    public EventList<ProjectEvent<?>> getEventList() {
        return eventList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AddWatchResult)) {
            return false;
        }
        AddWatchResult that = (AddWatchResult) o;
        return Objects.equals(eventList, that.eventList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventList);
    }
}
