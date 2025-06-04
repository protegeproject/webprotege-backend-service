package edu.stanford.protege.webprotege.issues;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.dispatch.*;
import edu.stanford.protege.webprotege.dispatch.validators.ProjectPermissionValidator;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import jakarta.annotation.Nonnull;
import jakarta.inject.Inject;

import java.util.Optional;

import static edu.stanford.protege.webprotege.access.BuiltInCapability.VIEW_OBJECT_COMMENT;


public class GetEntityEarliestCommentTimestampHandler
        implements ProjectActionHandler<GetEntityEarliestCommentTimestampAction, GetEntityEarliestCommentTimestampResult> {

    @Nonnull
    private final EntityDiscussionThreadRepository repository;

    @Nonnull
    private final AccessManager accessManager;

    @Inject
    public GetEntityEarliestCommentTimestampHandler(@Nonnull EntityDiscussionThreadRepository repository,
                                                    @Nonnull AccessManager accessManager) {
        this.repository = repository;
        this.accessManager = accessManager;
    }

    @Nonnull
    @Override
    public Class<GetEntityEarliestCommentTimestampAction> getActionClass() {
        return GetEntityEarliestCommentTimestampAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull GetEntityEarliestCommentTimestampAction action,
                                                @Nonnull RequestContext requestContext) {
        return new ProjectPermissionValidator(accessManager,
                                              action.projectId(),
                                              requestContext.getUserId(),
                                              VIEW_OBJECT_COMMENT.getCapability());
    }

    @Nonnull
    @Override
    public GetEntityEarliestCommentTimestampResult execute(
            @Nonnull GetEntityEarliestCommentTimestampAction action,
            @Nonnull ExecutionContext executionContext) {
        var entity = action.entity();
        var projectId = action.projectId();

        Optional<Long> earliestTs = repository.getEarliestCommentTimestamp(projectId, entity);

        return GetEntityEarliestCommentTimestampResult.create(
                earliestTs.orElse(null)
        );
    }
}
