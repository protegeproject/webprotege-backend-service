package edu.stanford.protege.webprotege.issues;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.dispatch.ProjectActionHandler;
import edu.stanford.protege.webprotege.dispatch.RequestContext;
import edu.stanford.protege.webprotege.dispatch.RequestValidator;
import edu.stanford.protege.webprotege.dispatch.validators.ProjectPermissionValidator;
import edu.stanford.protege.webprotege.entity.OWLEntityData;
import edu.stanford.protege.webprotege.event.EventList;
import edu.stanford.protege.webprotege.event.EventTag;
import edu.stanford.protege.webprotege.event.ProjectEvent;
import edu.stanford.protege.webprotege.events.EventManager;
import edu.stanford.protege.webprotege.mansyntax.render.HasGetRendering;
import edu.stanford.protege.webprotege.project.ProjectDetails;
import edu.stanford.protege.webprotege.project.ProjectDetailsRepository;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.webhook.CommentPostedSlackWebhookInvoker;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static edu.stanford.protege.webprotege.access.BuiltInAction.CREATE_OBJECT_COMMENT;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Oct 2016
 */
public class AddCommentHandler implements ProjectActionHandler<AddCommentAction, AddCommentResult> {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final HasGetRendering renderer;

    @Nonnull
    private final EventManager<ProjectEvent<?>> eventManager;

    @Nonnull
    private final EntityDiscussionThreadRepository repository;

    @Nonnull
    private final CommentNotificationEmailer notificationsEmailer;

    @Nonnull
    private final CommentPostedSlackWebhookInvoker commentPostedSlackWebhookInvoker;

    @Nonnull
    private final ProjectDetailsRepository projectDetailsRepository;

    @Nonnull
    private final AccessManager accessManager;

    @Inject
    public AddCommentHandler(@Nonnull ProjectId projectId,
                             @Nonnull HasGetRendering renderer,
                             @Nonnull EventManager<ProjectEvent<?>> eventManager,
                             @Nonnull EntityDiscussionThreadRepository repository,
                             @Nonnull CommentNotificationEmailer notificationsEmailer,
                             @Nonnull CommentPostedSlackWebhookInvoker commentPostedSlackWebhookInvoker,
                             @Nonnull ProjectDetailsRepository projectDetailsRepository,
                             @Nonnull AccessManager accessManager) {
        this.projectId = projectId;
        this.renderer = renderer;
        this.eventManager = eventManager;
        this.repository = repository;
        this.notificationsEmailer = notificationsEmailer;
        this.commentPostedSlackWebhookInvoker = commentPostedSlackWebhookInvoker;
        this.projectDetailsRepository = projectDetailsRepository;
        this.accessManager = accessManager;
    }

    @Nonnull
    @Override
    public Class<AddCommentAction> getActionClass() {
        return AddCommentAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull AddCommentAction action, @Nonnull RequestContext requestContext) {
        return new ProjectPermissionValidator(accessManager,
                                              projectId,
                                              requestContext.getUserId(),
                                              CREATE_OBJECT_COMMENT.getActionId());

    }

    @Nonnull
    @Override
    public AddCommentResult execute(@Nonnull AddCommentAction action,
                                    @Nonnull ExecutionContext executionContext) {
        UserId createdBy = executionContext.getUserId();
        long createdAt = System.currentTimeMillis();
        CommentRenderer r = new CommentRenderer();
        String rawComment = action.getComment();
        String renderedComment = r.renderComment(rawComment);
        Comment comment = new Comment(CommentId.create(),
                                      createdBy,
                                      createdAt,
                                      Optional.empty(),
                                      rawComment,
                                      renderedComment);
        ThreadId threadId = action.getThreadId();
        repository.addCommentToThread(threadId, comment);
        EventTag startTag = eventManager.getCurrentTag();
        postCommentPostedEvent(threadId, comment);
        EventList<ProjectEvent<?>> eventList = eventManager.getEventsFromTag(startTag);
        sendOutNotifications(threadId, comment);
        return AddCommentResult.create(action.getProjectId(), threadId, comment, renderedComment, eventList);

    }

    private void sendOutNotifications(ThreadId threadId, Comment comment) {
        Thread t = new Thread(() -> repository.getThread(threadId).ifPresent(thread -> {
            notificationsEmailer.sendCommentPostedNotification(projectId,
                                                               renderer.getRendering(thread.getEntity()),
                                                               thread,
                                                               comment);
            commentPostedSlackWebhookInvoker.invoke(projectId,
                                                    projectDetailsRepository.findOne(projectId).map(
                                                            ProjectDetails::getDisplayName).orElse("Project"),
                                                    renderer.getRendering(thread.getEntity()),
                                                    comment);
        }));
        t.start();
    }


    /**
     * Post a {@link CommentPostedEvent} to the project event bus.
     *
     * @param threadId The thread that the comment was added to.
     * @param comment  The comment that was added.
     */
    private void postCommentPostedEvent(@Nonnull ThreadId threadId,
                                        @Nonnull Comment comment) {
        Optional<EntityDiscussionThread> thread = repository.getThread(threadId);
        thread.ifPresent(t -> {
            OWLEntityData entityData = renderer.getRendering(t.getEntity());
            int commentCount = repository.getCommentsCount(projectId, t.getEntity());
            int openCommentCount = repository.getOpenCommentsCount(projectId, t.getEntity());
            CommentPostedEvent event = new CommentPostedEvent(projectId,
                                                              threadId,
                                                              comment,
                                                              Optional.of(entityData),
                                                              commentCount,
                                                              openCommentCount);
            eventManager.postEvent(event);

        });
    }

}
