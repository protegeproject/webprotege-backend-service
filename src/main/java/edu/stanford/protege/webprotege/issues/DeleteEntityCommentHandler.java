package edu.stanford.protege.webprotege.issues;

import edu.stanford.protege.webprotege.dispatch.ProjectActionHandler;
import edu.stanford.protege.webprotege.dispatch.RequestContext;
import edu.stanford.protege.webprotege.dispatch.RequestValidationResult;
import edu.stanford.protege.webprotege.dispatch.RequestValidator;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 Oct 2016
 */
public class DeleteEntityCommentHandler implements ProjectActionHandler<DeleteCommentAction, DeleteCommentResult> {

    @Nonnull
    private final EntityDiscussionThreadRepository repository;

    @Inject
    public DeleteEntityCommentHandler(@Nonnull EntityDiscussionThreadRepository repository) {
        this.repository = repository;
    }

    @Nonnull
    @Override
    public Class<DeleteCommentAction> getActionClass() {
        return DeleteCommentAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull DeleteCommentAction action, @Nonnull RequestContext requestContext) {
        return () -> {
            Optional<EntityDiscussionThread> thread = repository.findThreadByCommentId(action.commentId());
            if(!thread.isPresent()) {
                return getInvalidRequest();
            }
            long commentCount = thread.get().getComments().stream()
                    .filter(c -> c.getCreatedBy().equals(requestContext.getUserId()))
                    .filter(c -> c.getId().equals(action.commentId()))
                    .count();
            if(commentCount != 1L) {
                return getInvalidRequest();
            }
            return RequestValidationResult.getValid();
        };
    }

    private RequestValidationResult getInvalidRequest() {
        return RequestValidationResult.getInvalid("You do not have permission the delete this comment");
    }

    @Nonnull
    @Override
    public DeleteCommentResult execute(@Nonnull DeleteCommentAction action, @Nonnull ExecutionContext executionContext) {
        boolean deleted = repository.deleteComment(action.commentId());
        return new DeleteCommentResult(action.commentId(), deleted);
    }
}
