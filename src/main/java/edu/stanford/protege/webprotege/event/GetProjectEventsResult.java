package edu.stanford.protege.webprotege.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.Result;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/03/2013
 */
@AutoValue

@JsonTypeName("GetProjectEvents")
public abstract class GetProjectEventsResult implements Result {

    @JsonCreator
    public static GetProjectEventsResult create(@JsonProperty("events") EventList<?> events) {
        return new AutoValue_GetProjectEventsResult(events);
    }

    public abstract EventList<?> getEvents();

}
