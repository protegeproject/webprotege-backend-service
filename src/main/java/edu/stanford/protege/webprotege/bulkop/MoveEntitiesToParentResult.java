package edu.stanford.protege.webprotege.bulkop;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;

import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.event.EventList;
import edu.stanford.protege.webprotege.event.HasEventList;
import edu.stanford.protege.webprotege.event.ProjectEvent;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Sep 2018
 */
@AutoValue

@JsonTypeName("MoveEntitiesToParent")
public abstract class MoveEntitiesToParentResult implements Result, HasEventList<ProjectEvent<?>> {

    @JsonCreator
    public static MoveEntitiesToParentResult create(@JsonProperty("eventList") @Nonnull EventList<ProjectEvent<?>> eventList) {
        return new AutoValue_MoveEntitiesToParentResult(eventList);
    }

}
