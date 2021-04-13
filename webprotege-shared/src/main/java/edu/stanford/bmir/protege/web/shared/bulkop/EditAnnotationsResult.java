package edu.stanford.bmir.protege.web.shared.bulkop;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.HasEventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Sep 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("EditAnnotations")
public abstract class EditAnnotationsResult implements Result, HasEventList<ProjectEvent<?>> {

    @JsonCreator
    @Nonnull
    public static EditAnnotationsResult get(@JsonProperty("eventList") @Nonnull EventList<ProjectEvent<?>> eventList) {
        return new AutoValue_EditAnnotationsResult(eventList);
    }
}
