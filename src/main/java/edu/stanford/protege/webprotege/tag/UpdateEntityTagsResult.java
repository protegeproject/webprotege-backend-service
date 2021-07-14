package edu.stanford.protege.webprotege.tag;

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
 * 21 Mar 2018
 */
@AutoValue

@JsonTypeName("UpdateEntityTags")
public abstract class UpdateEntityTagsResult implements Result, HasEventList<ProjectEvent<?>> {

    @JsonCreator
    public static UpdateEntityTagsResult create(@JsonProperty("eventList") EventList<ProjectEvent<?>> eventList) {
        return new AutoValue_UpdateEntityTagsResult(eventList);
    }

    @Override
    public abstract EventList<ProjectEvent<?>> getEventList();
}
