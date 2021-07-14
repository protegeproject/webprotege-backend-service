package edu.stanford.protege.webprotege.watches;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.event.EventList;
import edu.stanford.protege.webprotege.event.HasEventList;
import edu.stanford.protege.webprotege.event.ProjectEvent;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 29/02/16
 */
@AutoValue

@JsonTypeName("SetEntityWatches")
public abstract class SetEntityWatchesResult implements Result, HasEventList<ProjectEvent<?>> {

    @JsonCreator
    public static SetEntityWatchesResult create(@JsonProperty("eventList") EventList<ProjectEvent<?>> eventList) {
        return new AutoValue_SetEntityWatchesResult(eventList);
    }

    @Override
    public abstract EventList<ProjectEvent<?>> getEventList();
}
