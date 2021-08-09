package edu.stanford.protege.webprotege.issues;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.event.EventList;
import edu.stanford.protege.webprotege.event.EventTag;
import edu.stanford.protege.webprotege.event.ProjectEvent;
import edu.stanford.protege.webprotege.events.EventManager;
import edu.stanford.protege.webprotege.common.ProjectId;

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
public class EditCommentActionHandler extends AbstractProjectActionHandler<EditCommentAction, EditCommentResult> {


    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final EntityDiscussionThreadRepository repository;

    @Nonnull
    private final EventManager<ProjectEvent<?>> eventManager;


    @Inject
    public EditCommentActionHandler(@Nonnull AccessManager accessManager,
                                    @Nonnull ProjectId projectId,
                                    @Nonnull EntityDiscussionThreadRepository repository,
                                    @Nonnull EventManager<ProjectEvent<?>> eventManager) {
        super(accessManager);
        this.projectId = projectId;
        this.repository = repository;
        this.eventManager = eventManager;
    }

    @Nonnull
    @Override
    public Class<EditCommentAction> getActionClass() {
        return EditCommentAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(EditCommentAction action) {
        return EDIT_OWN_OBJECT_COMMENT;
    }

    @Nonnull
    @Override
    public EditCommentResult execute(@Nonnull EditCommentAction action,
                                     @Nonnull ExecutionContext executionContext) {
        EventTag fromTag = eventManager.getCurrentTag();

        Optional<EntityDiscussionThread> thread = repository.getThread(action.getThreadId());
        if (!thread.isPresent()) {
            throw new RuntimeException("Invalid comment thread");
        }
        EntityDiscussionThread t = thread.get();
        String renderedComment = new CommentRenderer().renderComment(action.getBody());
        Optional<Comment> updatedComment = t.getComments().stream()
                                            .filter(c -> c.getId().equals(action.getCommentId()))
                                            .limit(1)
                                            .map(c -> new Comment(c.getId(),
                                                                  c.getCreatedBy(),
                                                                  c.getCreatedAt(),
                                                                  Optional.of(System.currentTimeMillis()),
                                                                  action.getBody(),
                                                                  renderedComment))
                                            .peek(c -> repository.updateComment(t.getId(), c))
                                            .findFirst();
        updatedComment.ifPresent(comment -> eventManager.postEvent(new CommentUpdatedEvent(projectId, t.getId(), comment)));
        EventList<ProjectEvent<?>> eventList = eventManager.getEventsFromTag(fromTag);
        return EditCommentResult.create(updatedComment, eventList);
    }

}
