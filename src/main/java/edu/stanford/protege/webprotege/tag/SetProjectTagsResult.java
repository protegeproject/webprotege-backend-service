package edu.stanford.protege.webprotege.tag;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;


import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.event.EventList;
import edu.stanford.protege.webprotege.event.HasEventList;
import edu.stanford.protege.webprotege.event.ProjectEvent;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Mar 2018
 */
@AutoValue

@JsonTypeName("SetProjectTags")
public abstract class SetProjectTagsResult implements Result, HasEventList<ProjectEvent<?>> {


    @JsonCreator
    public static SetProjectTagsResult create(@JsonProperty("eventList") @Nonnull EventList<ProjectEvent<?>> eventList) {
        return new AutoValue_SetProjectTagsResult(eventList);
    }
}
