package edu.stanford.protege.webprotege.issues;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.common.EventId;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.entity.OWLEntityData;
import edu.stanford.protege.webprotege.ipc.EventDispatcher;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.mansyntax.render.HasGetRendering;
import edu.stanford.protege.webprotege.project.ProjectDetails;
import edu.stanford.protege.webprotege.project.ProjectDetailsRepository;
import edu.stanford.protege.webprotege.webhook.CommentPostedSlackWebhookInvoker;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import static edu.stanford.protege.webprotege.access.BuiltInAction.CREATE_OBJECT_COMMENT;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Oct 2016
 */
public class CreateEntityDiscussionThreadHandler extends AbstractProjectActionHandler<CreateEntityDiscussionThreadAction, CreateEntityDiscussionThreadResult> {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final EntityDiscussionThreadRepository repository;

    @Nonnull
    private final ProjectDetailsRepository projectDetailsRepository;

    @Nonnull
    private final CommentNotificationEmailer notificationsEmailer;

    @Nonnull
    private final CommentPostedSlackWebhookInvoker commentPostedSlackWebhookInvoker;

    @Nonnull
    private final HasGetRendering renderer;

    @Nonnull
    private final EventDispatcher eventDispatcher;


    @Inject
    public CreateEntityDiscussionThreadHandler(@Nonnull AccessManager accessManager,
                                               @Nonnull ProjectId projectId,
                                               @Nonnull EntityDiscussionThreadRepository repository,
                                               @Nonnull ProjectDetailsRepository projectDetailsRepository,
                                               @Nonnull CommentNotificationEmailer notificationsEmailer,
                                               @Nonnull CommentPostedSlackWebhookInvoker commentPostedSlackWebhookInvoker,
                                               @Nonnull HasGetRendering renderer,
                                               @Nonnull EventDispatcher eventDispatcher) {
        super(accessManager);
        this.projectId = projectId;
        this.repository = repository;
        this.projectDetailsRepository = projectDetailsRepository;
        this.notificationsEmailer = notificationsEmailer;
        this.commentPostedSlackWebhookInvoker = commentPostedSlackWebhookInvoker;
        this.renderer = renderer;
        this.eventDispatcher = eventDispatcher;
    }

    @Nonnull
    @Override
    public Class<CreateEntityDiscussionThreadAction> getActionClass() {
        return CreateEntityDiscussionThreadAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(CreateEntityDiscussionThreadAction action) {
        return CREATE_OBJECT_COMMENT;
    }

    @Nonnull
    @Override
    public CreateEntityDiscussionThreadResult execute(@Nonnull CreateEntityDiscussionThreadAction action,
                                                      @Nonnull ExecutionContext executionContext) {
        String rawComment = action.comment();
        CommentRenderer commentRenderer = new CommentRenderer();
        String renderedComment = commentRenderer.renderComment(rawComment);
        UserId commentingUser = executionContext.userId();
        Comment comment = new Comment(CommentId.create(),
                                      commentingUser,
                                      System.currentTimeMillis(),
                                      Optional.empty(),
                                      rawComment,
                                      renderedComment);
        OWLEntity entity = action.entity();
        EntityDiscussionThread thread = new EntityDiscussionThread(ThreadId.create(),
                                                                   action.projectId(),
                                                                   entity,
                                                                   Status.OPEN,
                                                                   ImmutableList.of(comment));
        repository.saveThread(thread);
        int commentCount = repository.getCommentsCount(projectId, entity);
        int openCommentCount = repository.getOpenCommentsCount(projectId, entity);
        Optional<OWLEntityData> rendering = Optional.of(renderer.getRendering(entity));
        var event = new CommentPostedEvent(EventId.generate(),
                                           projectId,
                                           thread.getId(),
                                           comment,
                                           rendering,
                                           commentCount,
                                           openCommentCount);
        eventDispatcher.dispatchEvent(event);
        setOutNotifications(thread, comment);
        List<EntityDiscussionThread> threads = repository.findThreads(projectId, entity);
        return new CreateEntityDiscussionThreadResult(ImmutableList.copyOf(threads));
    }

    private void setOutNotifications(EntityDiscussionThread thread, Comment comment) {
        // TODO: Move this to a different service to listen for comment posted event
        Thread t = new Thread(() -> {
            notificationsEmailer.sendCommentPostedNotification(projectId,
                                                               renderer.getRendering(thread.getEntity()),
                                                               thread,
                                                               comment);
            commentPostedSlackWebhookInvoker.invoke(projectId,
                                                    projectDetailsRepository.findOne(projectId)
                                                                            .map(ProjectDetails::getDisplayName)
                                                                            .orElse("Project"),
                                                    renderer.getRendering(thread.getEntity()),
                                                    comment);
        });
        t.start();
    }
}
