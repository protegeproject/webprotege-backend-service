package edu.stanford.protege.webprotege.issues;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.events.EventManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.event.EventList;
import edu.stanford.protege.webprotege.event.EventTag;
import edu.stanford.protege.webprotege.event.ProjectEvent;
import edu.stanford.protege.webprotege.project.ProjectId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Optional;

import static edu.stanford.protege.webprotege.access.BuiltInAction.SET_OBJECT_COMMENT_STATUS;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Oct 2016
 */
public class SetDiscussionThreadStatusHandler extends AbstractProjectActionHandler<SetDiscussionThreadStatusAction, SetDiscussionThreadStatusResult> {

    @Nonnull
    private final EntityDiscussionThreadRepository repository;

    @Nonnull
    private final EventManager<ProjectEvent<?>> eventManager;

    @Nonnull
    private final ProjectId projectId;

    @Inject
    public SetDiscussionThreadStatusHandler(@Nonnull AccessManager accessManager,
                                            @Nonnull EntityDiscussionThreadRepository repository,
                                            @Nonnull EventManager<ProjectEvent<?>> eventManager,
                                            @Nonnull ProjectId projectId) {
        super(accessManager);
        this.repository = repository;
        this.eventManager = eventManager;
        this.projectId = projectId;
    }

    @Nonnull
    @Override
    public Class<SetDiscussionThreadStatusAction> getActionClass() {
        return SetDiscussionThreadStatusAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(SetDiscussionThreadStatusAction action) {
        return SET_OBJECT_COMMENT_STATUS;
    }

    @Nonnull
    @Override
    public SetDiscussionThreadStatusResult execute(@Nonnull SetDiscussionThreadStatusAction action,
                                                   @Nonnull ExecutionContext executionContext) {
        EventTag fromTag = eventManager.getCurrentTag();
        ThreadId threadId = action.getThreadId();
        Status status = action.getStatus();
        Optional<EntityDiscussionThread> thread = repository.setThreadStatus(threadId, status);
        int openComments = thread.map(t -> repository.getOpenCommentsCount(projectId, t.getEntity())).orElse(-1);
        eventManager.postEvent(new DiscussionThreadStatusChangedEvent(projectId,
                                                                                   threadId,
                                                                                   thread.map(EntityDiscussionThread::getEntity),
                                                                                   openComments,
                                                                                   status));
        EventList<ProjectEvent<?>> eventList = eventManager.getEventsFromTag(fromTag);
        return new SetDiscussionThreadStatusResult(threadId, status, eventList);
    }
}
