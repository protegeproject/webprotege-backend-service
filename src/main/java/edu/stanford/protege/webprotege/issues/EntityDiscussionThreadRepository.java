package edu.stanford.protege.webprotege.issues;

import edu.stanford.protege.webprotege.inject.ApplicationSingleton;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import jakarta.annotation.Nonnull;
import jakarta.inject.Inject;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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

    private Map<ProjectId, Map<IRI, List<Comment>>> cache = new ConcurrentHashMap<>();

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
        List<Comment> comments = getOpenedCommentsForProject(projectId).get(entity.getIRI());
        return comments == null ? 0 : comments.size();
    }


    public synchronized Map<IRI, List<Comment>> getOpenedCommentsForProject(ProjectId projectId) {
        Map<IRI, List<Comment>> response = this.cache.get(projectId);
        if(response != null) {
            return response;
        } else {
            response = new HashMap<>();
        }

        var query = query(where(PROJECT_ID).is(projectId)
                .and(STATUS).is(Status.OPEN));
        Map<IRI, List<Comment>> finalResponse = response;
        mongoTemplate.find(query, EntityDiscussionThread.class)
                .forEach(entityDiscussionThread -> {
                    List<Comment> existingComments = finalResponse.get(entityDiscussionThread.getEntity().getIRI());
                    if(existingComments == null) {
                        existingComments = new ArrayList<>();
                    }
                    existingComments.addAll(entityDiscussionThread.getComments());
                    finalResponse.put(entityDiscussionThread.getEntity().getIRI(), existingComments);
                });
        this.cache.put(projectId, response);
        return response;
    }


    public void saveThread(@Nonnull EntityDiscussionThread thread) {
        mongoTemplate.save(thread);
    }

    public void addCommentToThread(@Nonnull ThreadId threadId,
                                   @Nonnull ProjectId projectId,
                                   @Nonnull Comment comment) {
        var query = createQueryForThread(threadId);
        this.cache.remove(projectId);
        mongoTemplate.updateFirst(query, new Update().push(COMMENTS, comment), EntityDiscussionThread.class);
    }

    public Optional<EntityDiscussionThread> setThreadStatus(@Nonnull ThreadId threadId,
                                                            ProjectId projectId,
                                                            @Nonnull Status status) {
        var query = createQueryForThread(threadId);
        this.cache.remove(projectId);
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

    public boolean deleteComment(ProjectId projectId, CommentId commentId) {
        var query = query(where(COMMENTS_ID).is(commentId));
        this.cache.remove(projectId);
        return mongoTemplate.updateFirst(query, new Update().pull(COMMENTS, query(where("_id").is(commentId))),
                                  EntityDiscussionThread.class).getModifiedCount() == 1;
    }

    public List<EntityDiscussionThread> getThreadsInProject(ProjectId projectId) {
        return mongoTemplate.find(query(where(PROJECT_ID).is(projectId)),
                                  EntityDiscussionThread.class);
    }
}
