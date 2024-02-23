package edu.stanford.protege.webprotege.issues;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.common.EventId;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.EventDispatcher;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

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
    private final ProjectId projectId;

    @Nonnull
    private final EventDispatcher eventDispatcher;

    @Inject
    public SetDiscussionThreadStatusHandler(@Nonnull AccessManager accessManager,
                                            @Nonnull EntityDiscussionThreadRepository repository,
                                            @Nonnull ProjectId projectId,
                                            @Nonnull EventDispatcher eventDispatcher) {
        super(accessManager);
        this.repository = repository;
        this.eventDispatcher = eventDispatcher;
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
        var threadId = action.threadId();
        var status = action.status();
        var thread = repository.setThreadStatus(threadId, status);
        int openComments = thread.map(t -> repository.getOpenCommentsCount(projectId, t.getEntity())).orElse(-1);
        eventDispatcher.dispatchEvent(new DiscussionThreadStatusChangedEvent(EventId.generate(),
                                                                             projectId,
                                                                             threadId,
                                                                             thread.map(EntityDiscussionThread::getEntity),
                                                                             openComments,
                                                                             status));
        return new SetDiscussionThreadStatusResult(threadId, status);
    }
}
