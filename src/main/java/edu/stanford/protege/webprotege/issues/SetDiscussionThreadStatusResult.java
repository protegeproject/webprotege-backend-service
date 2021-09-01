package edu.stanford.protege.webprotege.issues;


import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.event.EventList;
import edu.stanford.protege.webprotege.event.HasEventList;
import edu.stanford.protege.webprotege.common.ProjectEvent;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Oct 2016
 */
public class SetDiscussionThreadStatusResult implements Result, HasEventList<ProjectEvent> {

    private ThreadId threadId;

    private Status result;

    private EventList<ProjectEvent> eventList;

    public SetDiscussionThreadStatusResult(@Nonnull ThreadId threadId,
                                           @Nonnull Status result,
                                           @Nonnull EventList<ProjectEvent> eventList) {
        this.threadId = checkNotNull(threadId);
        this.result = checkNotNull(result);
        this.eventList = checkNotNull(eventList);
    }


    private SetDiscussionThreadStatusResult() {
    }

    @Nonnull
    public ThreadId getThreadId() {
        return threadId;
    }

    @Nonnull
    public Status getResult() {
        return result;
    }

    @Override
    public EventList<ProjectEvent> getEventList() {
        return eventList;
    }
}
