package edu.stanford.bmir.protege.web.shared.watches;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.HasEventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 29/02/16
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("SetEntityWatches")
public abstract class SetEntityWatchesResult implements Result, HasEventList<ProjectEvent<?>> {

    @JsonCreator
    public static SetEntityWatchesResult create(@JsonProperty("eventList") EventList<ProjectEvent<?>> eventList) {
        return new AutoValue_SetEntityWatchesResult(eventList);
    }

    @Override
    public abstract EventList<ProjectEvent<?>> getEventList();
}
