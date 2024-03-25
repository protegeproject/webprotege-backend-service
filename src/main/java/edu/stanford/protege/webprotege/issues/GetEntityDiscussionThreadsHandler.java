package edu.stanford.protege.webprotege.issues;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.dispatch.ProjectActionHandler;
import edu.stanford.protege.webprotege.dispatch.RequestContext;
import edu.stanford.protege.webprotege.dispatch.RequestValidator;
import edu.stanford.protege.webprotege.dispatch.validators.ProjectPermissionValidator;
import edu.stanford.protege.webprotege.entity.OWLEntityData;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.renderer.RenderingManager;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;

import static edu.stanford.protege.webprotege.access.BuiltInAction.VIEW_OBJECT_COMMENT;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Oct 2016
 */
public class GetEntityDiscussionThreadsHandler implements ProjectActionHandler<GetEntityDiscussionThreadsAction, GetEntityDiscussionThreadsResult> {

    @Nonnull
    private final EntityDiscussionThreadRepository repository;

    @Nonnull
    private final AccessManager accessManager;

    @Nonnull
    private final RenderingManager renderingManager;

    @Inject
    public GetEntityDiscussionThreadsHandler(@Nonnull EntityDiscussionThreadRepository repository,
                                             @Nonnull AccessManager accessManager,
                                             @Nonnull RenderingManager renderingManager) {
        this.repository = repository;
        this.accessManager = accessManager;
        this.renderingManager = renderingManager;
    }

    @Nonnull
    @Override
    public Class<GetEntityDiscussionThreadsAction> getActionClass() {
        return GetEntityDiscussionThreadsAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull GetEntityDiscussionThreadsAction action, @Nonnull RequestContext requestContext) {
        return new ProjectPermissionValidator(accessManager,
                                              action.projectId(),
                                              requestContext.getUserId(),
                                              VIEW_OBJECT_COMMENT.getActionId());
    }

    @Nonnull
    @Override
    public GetEntityDiscussionThreadsResult execute(@Nonnull GetEntityDiscussionThreadsAction action, @Nonnull ExecutionContext executionContext) {
        List<EntityDiscussionThread> threads = repository.findThreads(action.projectId(), action.entity());
        OWLEntityData entityData = renderingManager.getRendering(action.entity());
        return new GetEntityDiscussionThreadsResult(entityData,
                                                    ImmutableList.copyOf(threads));
    }
}
