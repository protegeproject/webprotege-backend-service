package edu.stanford.protege.webprotege.form;

import com.google.auto.value.AutoValue;

import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.event.EventList;
import edu.stanford.protege.webprotege.event.HasEventList;
import edu.stanford.protege.webprotege.event.ProjectEvent;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-10-22
 */
@AutoValue

public abstract class DeprecateEntityByFormResult implements Result, HasEventList<ProjectEvent<?>> {

    public static DeprecateEntityByFormResult get(EventList<ProjectEvent<?>> eventList) {
        return new AutoValue_DeprecateEntityByFormResult(eventList);
    }

    @Override
    @Nonnull
    public abstract EventList<ProjectEvent<?>> getEventList();
}
