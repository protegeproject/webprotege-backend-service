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
 * 24 Sep 2018
 */
@AutoValue

@JsonTypeName("SetAnnotationValue")
public abstract class SetAnnotationValueResult implements Result, HasEventList<ProjectEvent<?>> {

    @JsonCreator
    public static SetAnnotationValueResult create(@JsonProperty("eventList") @Nonnull EventList<ProjectEvent<?>> eventList) {
        return new AutoValue_SetAnnotationValueResult(eventList);
    }
}
