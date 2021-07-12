package edu.stanford.protege.webprotege.issues;

import com.mongodb.BasicDBObject;
import edu.stanford.protege.webprotege.inject.ApplicationSingleton;
import edu.stanford.protege.webprotege.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.UpdateDefinition;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.protege.webprotege.issues.EntityDiscussionThread.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Oct 2016
 */
@ApplicationSingleton
public class EntityDiscussionThreadRepository {

    public static final String MATCHED_COMMENT_PATH = "comments.$";

    private final MongoTemplate mongoTemplate;

    @Inject
    public EntityDiscussionThreadRepository(@Nonnull MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<EntityDiscussionThread> findThreads(@Nonnull ProjectId projectId,
                                                    @Nonnull OWLEntity entity) {
        var query = query(where(PROJECT_ID).is(projectId).and(ENTITY).is(entity)).with(Sort.by("comments.0.createdAt"));
        return mongoTemplate.find(query, EntityDiscussionThread.class);
    }

    public int getCommentsCount(@Nonnull ProjectId projectId,
                                @Nonnull OWLEntity entity) {
        return findThreads(projectId, entity).stream()
                                             .map(thread -> thread.getComments().size())
                                             .reduce((left, right) -> left + right)
                                             .orElse(0);
    }

    public int getOpenCommentsCount(@Nonnull ProjectId projectId,
                                    @Nonnull OWLEntity entity) {
        var query = query(where(PROJECT_ID).is(projectId)
                      .and(ENTITY).is(entity).and(STATUS).is(Status.OPEN));
        return mongoTemplate.find(query, EntityDiscussionThread.class)
                        .stream().map(thread -> thread.getComments().size())
                        .reduce((left, right) -> left + right)
                        .orElse(0);
    }

    public void saveThread(@Nonnull EntityDiscussionThread thread) {
        mongoTemplate.save(thread);
    }

    public void addCommentToThread(@Nonnull ThreadId threadId,
                                   @Nonnull Comment comment) {
        var query = createQueryForThread(threadId);
        mongoTemplate.updateFirst(query, new Update().push(COMMENTS, comment), EntityDiscussionThread.class);
    }

    public Optional<EntityDiscussionThread> setThreadStatus(@Nonnull ThreadId threadId,
                                                            @Nonnull Status status) {
        var query = createQueryForThread(threadId);
        mongoTemplate.updateFirst(query, update(STATUS, status), EntityDiscussionThread.class);
        return Optional.ofNullable(mongoTemplate.findOne(query, EntityDiscussionThread.class));
    }


    public Optional<EntityDiscussionThread> getThread(@Nonnull ThreadId id) {
        return Optional.ofNullable(mongoTemplate.findOne(createQueryForThread(id), EntityDiscussionThread.class));
    }

    public void replaceEntity(ProjectId projectId, OWLEntity entity, OWLEntity withEntity) {
        var query = query(where(PROJECT_ID).is(projectId).and(ENTITY).is(entity));
        mongoTemplate.updateMulti(query, update(ENTITY, withEntity), EntityDiscussionThread.class);
    }


    private Query createQueryForThread(ThreadId threadId) {
        return query(where("_id").is(threadId));
    }

    public void updateComment(ThreadId id, Comment comment) {
        var query = createQueryForThread(id)
                .addCriteria(where(COMMENTS_ID).is(comment.getId()));
        mongoTemplate.updateFirst(query, update(MATCHED_COMMENT_PATH, comment), EntityDiscussionThread.class);
    }

    public Optional<EntityDiscussionThread> findThreadByCommentId(CommentId commentId) {
        var query = query(where(COMMENTS_ID).is(commentId));
        return Optional.ofNullable(mongoTemplate.findOne(query, EntityDiscussionThread.class));
    }

    public boolean deleteComment(CommentId commentId) {
        var query = query(where(COMMENTS_ID).is(commentId));
        return mongoTemplate.updateFirst(query, new Update().pull(COMMENTS, query(where("_id").is(commentId))),
                                  EntityDiscussionThread.class).getModifiedCount() == 1;
    }

    public List<EntityDiscussionThread> getThreadsInProject(ProjectId projectId) {
        return mongoTemplate.find(query(where(PROJECT_ID).is(projectId)),
                                  EntityDiscussionThread.class);
    }
}
