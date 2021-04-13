package edu.stanford.bmir.protege.web.shared.tag;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.HasEventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Mar 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("UpdateEntityTags")
public abstract class UpdateEntityTagsResult implements Result, HasEventList<ProjectEvent<?>> {

    @JsonCreator
    public static UpdateEntityTagsResult create(@JsonProperty("eventList") EventList<ProjectEvent<?>> eventList) {
        return new AutoValue_UpdateEntityTagsResult(eventList);
    }

    @Override
    public abstract EventList<ProjectEvent<?>> getEventList();
}
