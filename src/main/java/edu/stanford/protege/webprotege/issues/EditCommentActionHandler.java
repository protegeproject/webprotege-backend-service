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
import java.util.Optional;

import static edu.stanford.protege.webprotege.access.BuiltInAction.EDIT_OWN_OBJECT_COMMENT;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Oct 2016
 */
public class EditCommentActionHandler extends AbstractProjectActionHandler<UpdateCommentAction, UpdateCommentResult> {


    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final EntityDiscussionThreadRepository repository;

    @Nonnull
    private final EventDispatcher eventDispatcher;

    @Inject
    public EditCommentActionHandler(@Nonnull AccessManager accessManager,
                                    @Nonnull ProjectId projectId,
                                    @Nonnull EntityDiscussionThreadRepository repository,
                                    @Nonnull EventDispatcher eventDispatcher) {
        super(accessManager);
        this.projectId = projectId;
        this.repository = repository;
        this.eventDispatcher = eventDispatcher;
    }

    @Nonnull
    @Override
    public Class<UpdateCommentAction> getActionClass() {
        return UpdateCommentAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(UpdateCommentAction action) {
        return EDIT_OWN_OBJECT_COMMENT;
    }

    @Nonnull
    @Override
    public UpdateCommentResult execute(@Nonnull UpdateCommentAction action,
                                     @Nonnull ExecutionContext executionContext) {
        Optional<EntityDiscussionThread> thread = repository.getThread(action.threadId());
        if (thread.isEmpty()) {
            throw new RuntimeException("Invalid comment thread");
        }
        var theThread = thread.get();
        String renderedComment = new CommentRenderer().renderComment(action.body());
        var updatedComment = theThread.getComments().stream()
                                            .filter(c -> c.getId().equals(action.commentId()))
                                            .limit(1)
                                            .map(c -> new Comment(c.getId(),
                                                                  c.getCreatedBy(),
                                                                  c.getCreatedAt(),
                                                                  Optional.of(System.currentTimeMillis()),
                                                                  action.body(),
                                                                  renderedComment))
                                            .peek(c -> repository.updateComment(theThread.getId(), c))
                                            .findFirst();
        updatedComment.ifPresent(comment -> eventDispatcher.dispatchEvent(new CommentUpdatedEvent(EventId.generate(), projectId, theThread.getId(), comment)));
        return new UpdateCommentResult(updatedComment);
    }

}
